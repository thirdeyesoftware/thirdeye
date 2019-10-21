#!/bin/bash
function help {
    echo "Usage: $(basename $0) <hostname|IP|pattern> [-p <gcp project>] [-f] [-r] [-c] [-t <minutes>] [-d] [-b] [-l] [-z zone] [-o] [-D] [-b] [-l] [-z zone] [-o] [-D] [-a <days>] [-x <port>]"
    echo options:
    echo "  -f  : optional: fast , do not do ssh config"
    echo "  -r  : optional: connect to the first instance that matches the pattern, do not show a menu"
    echo "  -c  : optional: clear the local cache and get new list from gcloud compute instance list.  If there is no previous cache, then it will go get a new instance list"
    echo "  -t  : optional: cacheAmountInMinutes , defaults to 10 minute. "
    echo "  -d  : optional: show only deployment types of b/g/a. etc,  for example enter -d b  to display all blue deployments"
    echo "  -D  : optional: show debug logging "
    echo "  -z  : optional:  specific zone "
    echo "  -b  : optional: beta zones  "
    echo "  -a <x> : optional: filter out vms older than X days "
    echo "  -l  : optional: all live zones "
    echo "  -q  : optional: run compute list with --quiet command "
    echo "  -i  : optional: add addition info like id,scheduling...etc "
    echo "  -o  : optional: show old vms first in the list"
    echo "  -p  <gcp project>: optional: gcp project"
    echo "  -P	<remote port> : optional : remote port to use for connection"
    echo "  -x  <port>: optional: proxy local traffic on specified port"
    echo "  -h  : show Help"
    echo "example: to login into any bass instance: $(basename $0) bass -r"
    echo "example: to use the cache:  $(basename $0) bass -f"
    echo "example: to keep using the cache for 200 mins instead of 2 mins default:  $(basename $0) bass -f -t 200"
    echo "example: to only list beta instances, enter ./gssh-bastion.sh collection -b"
    echo "example: to only list live(non-beta) instances, enter ./gssh-bastion.sh collection -l"
    echo "example: to only list beta instances with bluegreen deployments of type -g-, enter, ./gssh-bastion.sh pip -b -d g "
    echo "example: to show old vms first in the list, enter, ./gssh-bastion.sh bass -o "
    echo "example: find all vms older than 45 days, run"
    echo "             gssh-bastion.sh \"\" -p hd-product-discovery-prod -a 45 "
    echo "example: to display addition vm metadata like instance id,premptible..etc run :"
    echo "             gssh-bastion.sh loyalty-platform-orch -p hd-loyalty-dev -i id,scheduling"
    echo "example: to display addition vm metadata startupopts run :"
    echo "             gssh-bastion.sh loyalty-platform-orch -p hd-loyalty-dev  -i metadata.items['startUpOpts']"
    exit 1
}

[[ $# -eq 0 ]] && { help; exit 1; }
pattern="$1"
if [[ $pattern =~ ^- ]]; then echo "Search Pattern must not start with a \"-\"";help; fi

ZONES=us-central1-a,us-central1-b,us-central1-c,us-central1-f,us-east1-b,us-east1-c,us-east1-d,us-east4-a,us-east4-b,us-east4-c
BETA_ZONES=us-central1-f,us-east1-d
LIVE_ZONES=us-central1-b,us-central1-c,us-east1-b,us-east1-c
fast=false
showMenu=true
clearCache=false
cacheAmountInMinutes=10
remoteProxy=false
remoteProxyPort=9042
localproxy=false
localProxyPort=9042
actuallyUseCachedCopy=false
#DeploymentsType=.*
NormalEndingType='-....$'
CASSEnding='-[0-9]*$'
SortOrderType='~'
declare -a instance_name
declare -a instance_ip
declare -a project
project=$(gcloud config list project 2>/dev/null | grep -o -E '^project\s+=\s+(\S+)$' | sed 's/project = //g')
oldIFS="$IFS"

IFS='
'
#start with the second argument
OPTIND=2
while getopts "frcFRCt:p:d:z:blDoqP:x:a:i:" OPTION
do
    case $OPTION in
        f)  fast=true;;
        r)  showMenu=false;;
        c)  clearCache=true;;
        F)  fast=true;;
        R)  showMenu=false;;
        q)  quiet="--quiet";;
        z)  echo "ZONES: $OPTARG";OVERRIDE_ZONES=$OPTARG;;
        C)  echo "ClearLocalCache";clearCache=true;;
        t)  cacheAmountInMinutes=$OPTARG;;
        D)  echo "debug logging";set -x;;
        b)  echo "use beta zones";ONLY_BETA_ZONES=true;;
        l)  echo "use live zones";ONLY_LIVE_ZONES=true;;
        d)  echo "show deployment types \"$OPTARG\"";DeploymentsType=$OPTARG;;
        o)  echo "show old vms first in the list";SortOrderType='';;
        a)  echo "find vms older than `date -v -${OPTARG}d +%Y-%m-%d`"; filter="creationTimestamp<`date -v -${OPTARG}d +%Y-%m-%d`";;
        i)  infostring=,$OPTARG;;
        p)  project=$OPTARG;;
        P)  echo "remoteProxyPort $OPTARG";remoteProxy=true;remoteProxyPort=$OPTARG;;
        x)  echo "localProxyPort $OPTARG";localProxy=true;localProxyPort=$OPTARG;;
        h)  help;exit 0;;
        \?) help;exit 1;;
    esac
done
if [ ! -z "$ONLY_BETA_ZONES" ]; then
  OVERRIDE_ZONES=$OVERRIDE_ZONES,$BETA_ZONES
fi
if [ ! -z "$ONLY_BETA_ZONES" ]; then
  OVERRIDE_ZONES=$OVERRIDE_ZONES,$BETA_ZONES
fi
if [ ! -z "$ONLY_LIVE_ZONES" ]; then
  if [ ! -z ${OVERRIDE_ZONES+x} ]; then
    echo WARNING: filtering for live and beta zones...ie ..all zones
  fi
  OVERRIDE_ZONES=$OVERRIDE_ZONES,$LIVE_ZONES
fi
if [ -z ${OVERRIDE_ZONES+x} ]; then
  OVERRIDE_ZONES=$ZONES
else
echo filtering on zones $OVERRIDE_ZONES
fi

projectString="--project=$project"

file=/tmp/insts_`whoami`_${project}
if [[ -e "$file" && -s "$file" ]] ; then
  test "`find $file -mmin +$cacheAmountInMinutes`"
  if [ "$?" == "1" ]; then
     if [[ "$clearCache" == "false" && "$fast" == "true" ]]; then
       actuallyUseCachedCopy=true
       #echo "WARNING: you are using a cached version of instance list, use -c to get fresh list at $file"
     else
       actuallyUseCachedCopy=false
     fi
  else
     if [[ "$clearCache" == "false" && "$fast" == "true" ]]; then
       if [ -z "$quiet" ]; then
         echo "WARNING: Cached instance list is older than $cacheAmountInMinutes minute(s)."
       fi

    fi
  fi
fi

if [ "$fast" == "false" ]; then
  pub=$(cat ~/.ssh/google_compute_engine.pub)
  gcloud compute project-info describe --project $project | grep -q $pub
  found_key=$?
  if [[ $found_key -ne 0 ]]; then
    if [ -z "$quiet" ]; then
      echo "Need to add ssh key to project"
    fi
    gcloud compute config-ssh --project $project >> /dev/null
    gcloud compute config-ssh --remove --project $project >> /dev/null
  fi
fi
if [ "$project" == "hd-www-dev" ] || [ "$project" == "hd-www-stage" ] || [ "$project" == "hd-www-prod" ] ; then
  project=$(echo $project | sed 's/hd-www-//g')
  bastion_host="bastion.gcp-$project.homedepot.com"
elif [ "$project" == "hd-search-dev-187616" ] || [ "$project" == "hd-search-stage-187616" ] || [ "$project" == "hd-search-prod-187616" ] ; then
  project=$(echo $project | sed 's/-187616//g')
  bastion_host="bastion.$project.gcp.homedepot.com"
else
  bastion_host="bastion.$project.gcp.homedepot.com"
fi

if [ "$actuallyUseCachedCopy" == "false" ]; then
    if [ -z "$quiet" ]; then
      echo "New instance list cache location: $file"
      echo "Run next time with -f use this cached version"
    fi

    gcloud compute instances list $quiet $projectString --filter="$filter" --sort-by=${SortOrderType}creationTimestamp --format="csv(name,zone,networkInterfaces[0].networkIP,creationTimestamp.date("%Y-%m-%dT%H:%M:%S"):label=CREATED,metadata.items['version'],networkInterfaces[0].accessConfigs[0].natIP$infostring)" 2>/dev/null |tr "," "\t">$file

    sed -i -e 's/key=version;value=//g' $file
fi

# Show menu with the list of projects
function show_menu {

select myinstance in $(cat $file | grep "$pattern" |env bluegreen=$DeploymentsType NormalEndingType=$NormalEndingType CASSEnding=$CASSEnding perl -F\\t -lane 'print if ($F[0] =~ m/$ENV{bluegreen}$ENV{NormalEndingType}/) || ($F[0] =~ m/$ENV{bluegreen}$ENV{CASSEnding}/) || $ENV{bluegreen} eq ""' |env myzones="$OVERRIDE_ZONES" perl -F\\t -lane 'print if $ENV{myzones} =~ m/$F[1]/') QUIT ;
  do

    if [ "$myinstance" == "QUIT" ]; then exit 1; fi
    instance_name="$(echo $myinstance | awk -F'\t' '{print $1}')"
    instance_zone="$(echo $myinstance | awk -F'\t' '{print $2}')"
    instance_ip="$(echo $myinstance | awk -F'\t' '{print $3}')"

    if [ "$fast" == "false" ]; then
     gcloud compute instances describe $instance_name $projectString --format="csv(tags.items)" --zone $instance_zone | grep -q all-bastion-ssh
      found_tag=$?
      if [[ $found_tag -eq 0 ]]; then
        echo "$instance_name has the all-bastion-ssh tag"
      else
        echo "$instance_name DOES NOT have the all-bastion-ssh tag.  SSH via Bastion is unavailable until the instance is updated to include this tag."
        exit 1
      fi
    fi
    if [ -z "$quiet" ]; then
      echo "Connecting to $instance_name using Bastion"
    fi
    if [ "$remoteProxy" == "true" ]; then
      ssh -o UserKnownHostsFile=/dev/null -o StrictHostKeyChecking=no -i ~/.ssh/google_compute_engine -o ProxyCommand="ssh -o UserKnownHostsFile=/dev/null -o StrictHostKeyChecking=no -i ~/.ssh/google_compute_engine -W %h:%p $bastion_host" -L $localProxyPort:127.0.0.1:$remoteProxyPort $instance_ip
    else
      ssh -o UserKnownHostsFile=/dev/null -o StrictHostKeyChecking=no -i ~/.ssh/google_compute_engine -o ProxyCommand="ssh -o UserKnownHostsFile=/dev/null -o StrictHostKeyChecking=no -i ~/.ssh/google_compute_engine -W %h:%p $bastion_host" $instance_ip
    fi
    return 0
  done
}
function dont_show_menu {

  myinst=$(cat $file | grep "$pattern" |env bluegreen=$DeploymentsType NormalEndingType=$NormalEndingType CASSEnding=$CASSEnding perl -F\\t -lane 'print if ($F[0] =~ m/$ENV{bluegreen}$ENV{NormalEndingType}/) || ($F[0] =~ m/$ENV{bluegreen}$ENV{CASSEnding}/) || $ENV{bluegreen} eq ""' |env myzones="$OVERRIDE_ZONES" perl -F\\t -lane 'print if $ENV{myzones} =~ m/$F[1]/'| head -1)
  if [ ! -z "$myinst" ]; then
    instance_name="$(echo $myinst | awk -F'\t' '{print $1}')"
    instance_zone="$(echo $myinst | awk -F'\t' '{print $2}')"
    instance_ip="$(echo $myinst | awk -F'\t' '{print $3}')"

    if [ "$fast" == "false" ]; then
      gcloud compute instances describe $instance_name  $projectString --format="csv(tags.items)" --zone $instance_zone | grep -q all-bastion-ssh
      found_tag=$?
      if [[ $found_tag -eq 0 ]]; then
        echo "$instance_name has the all-bastion-ssh tag"
      else
        echo "$instance_name DOES NOT have the all-bastion-ssh tag.  SSH via Bastion is unavailable until the instance is updated to include this tag."
        exit 1
      fi
    fi
    if [ -z "$quiet" ]; then
      echo "Connecting to $instance_name using Bastion"
    fi
    if [ "$proxy" == "true" ]; then
      ssh -o UserKnownHostsFile=/dev/null -o StrictHostKeyChecking=no -i ~/.ssh/google_compute_engine -o ProxyCommand="ssh -o UserKnownHostsFile=/dev/null -o StrictHostKeyChecking=no -i ~/.ssh/google_compute_engine -W %h:%p $bastion_host" -L $localProxyPort:127.0.0.1:$remoteProxyPort $instance_ip
    else
      ssh -o UserKnownHostsFile=/dev/null -o StrictHostKeyChecking=no -i ~/.ssh/google_compute_engine -o ProxyCommand="ssh -o UserKnownHostsFile=/dev/null -o StrictHostKeyChecking=no -i ~/.ssh/google_compute_engine -W %h:%p $bastion_host" $instance_ip
    fi
  fi
  exit 0
}

if [ "$showMenu" == "false" ]; then
  if [ ! -z "$quiet" ]; then
    exit 0
  fi
  dont_show_menu
else
  show_menu
fi
