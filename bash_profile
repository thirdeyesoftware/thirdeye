source ~/.git-prompt.sh
source ~/.git-completion.bash
export GIT_PS1_SHOWCOLORHINTS=1
export GIT_PS1_SHOWDIRTYSTATE=1
export GIT_PS1_SHOWUPSTREAM="auto"

PATH=$PATH:~/workspaces/local-scripts/utilities-shell
export PATH

export CLICOLOR=1
export LSCOLORS=GxxxxxxxCxxxxxxxxxxxxx
alias ls='ls -Gh'
export GIT_PS1_SHOWCOLORHINTS=1
export GIT_PS1_SHOWDIRTYSTATE=1
export PATH="$PATH:$HOME/bin"

# Regular Colors
Black="\[\033[0;30m\]"        # Black
Grey="\[\033[1;30m\]"        # Black
Red="\[\033[0;31m\]"          # Red
lRed="\[\033[1;31m\]"          # Red
Green="\[\033[0;32m\]"        # Green
lGreen="\[\033[1;32m\]"        # Green
dGreen="\[\033[2;32m\]"        # Green
Yellow="\[\033[0;33m\]"       # Yellow
Blue="\[\033[0;34m\]"         # Blue
lBlue="\[\033[1;34m\]"         # Blue
Purple="\[\033[0;35m\]"       # Purple
Cyan="\[\033[0;36m\]"         # Cyan
lCyan="\[\033[1;36m\]"         # Cyan
White="\[\033[0;37m\]"        # White
Brown="\[\033[1;33m\]"    # Brown

export Emoji='$(if [[ $? == 0 ]]; then printf "\xf0\x9f\x98\x83"; else printf "\xF0\x9F\x98\xAD"; fi) \[\033[0;33m\] => \[\033[0;36m\]'
# export Emoji='$(if [[ $? == 0 ]]; then printf "\xf0\x9f\x98\x83"; else printf "\xF0\x9F\x98\xAD"; fi) \[\033[0;33m\] => \[\033[0;36m\] '
# export Emoji="$(if [[ $? == 0 ]]; then printf "\xf0\x9f\x98\x83"; else printf "\xF0\x9F\x98\xAD"; fi) $Yellow => $Cyan"

parse_git_branch() {
        git branch 2> /dev/null | sed -e '/^[^*]/d' -e 's/* \(.*\)/ (\1)/'
}
parse_git_status() {
    #if_we_are_in_git_work_tree
    if $(git rev-parse --is-inside-work-tree &> /dev/null)
    then
        local status=$(git status --short 2> /dev/null)
    local unstaged=$(git status | egrep "not staged|Untracked|Unmerged")
        if [ -n "${unstaged}" ]  # if unstaged changes exist, color red
        then
            PS1+=${Red}
        elif [ -n "${status}" ]  # else if changes exist but are all staged, yello
    then
        PS1+=${Yellow}
    else                     # no uncommitted changes, green
            PS1+=${Green}
        fi
        PS1+=$(parse_git_branch)
    fi
}
build_prompt() {
 if $(git rev-parse --is-inside-work-tree &> /dev/null)
    then
        PS1="${Grey}[\t] ${Yellow}$(pwd | sed s:$(git rev-parse --show-toplevel | egrep -o '/.*/')::g)"
    else
        PS1="${Grey}[\t] ${Yellow}\W"
    fi
    parse_git_status
    PS1+=" ${Cyan}${Emoji}"
}
    #stores function calls and executes prior to PS1 being set, allows you to cheat
PROMPT_COMMAND=build_prompt

#aliases
alias ..='cd ..'
alias ...='cd ../..'
alias ~='cd ~'
alias ls='ls -GFh'
alias lr='ls -hartl'
alias ll='ls -GFlha'
alias la='ls -a'
alias project='cd ../Shared/projects/2018/cloud/'
alias status='git status'
alias pull='git pull'
alias push='git push'
alias cout='git checkout'
alias commit='git commit'
alias add='git add'
alias stash='git stash'
alias pop='git stash pop'
alias branch='git branch'
alias c='clear'
#alias bash='~ && code .bash_profile'
alias bp='code ~/.bash_profile'
alias mvnci='mvn clean install'
alias mvncp='mvn clean package'
alias mvnvi='mvn verify -Pintegration'
alias mvnvis='mvn verify -Pintegration-search'
alias mvncib='mvn clean install -Dskip.unit.tests=true'
alias mvncii='mvn clean install -Pintegration'
alias mvnciis='mvn clean install -Pintegration-search'
alias gprice='gssh-bastion.sh cassandra-pricing-us-east1-c-non-seed-1 -x 9042'
alias gcost='/Users/jsb8516/workspaces/local-scripts/utilities-shell/gssh-bastion.sh cassandra-cost-us-east1-c-seed-1 -x 9042 -P 9142'
alias sshhpr='ssh jsb8516@hadpcs07@hadm1701.homedepot.com@pimssh.homedepot.com'

# PS1='\u $(if [[ $? == 0 ]]; then echo "\[\e[32m\]:)"; else echo "\[\e[31m\]:("; fi)\[\e[0m\] \w $ '

# Environment variables
export AUTH_DOMAIN='master-data-security.apps-np.homedepot.com'
export AUTH_PATH='/security/oauth/token'

# THD Credentials
export AUTH_CLIENT_ID='df2a7b2e-c6b9-41ce-a4ae-6d2f4547acd1'
export AUTH_CLIENT_SECRET='zuRVk357A4n8MfNf'

# The next line updates PATH for the Google Cloud SDK.
if [ -f '/Users/jsb8516/Downloads/google-cloud-sdk/path.bash.inc' ]; then . '/Users/jsb8516/Downloads/google-cloud-sdk/path.bash.inc'; fi

# The next line enables shell command completion for gcloud.
if [ -f '/Users/jsb8516/Downloads/google-cloud-sdk/completion.bash.inc' ]; then . '/Users/jsb8516/Downloads/google-cloud-sdk/completion.bash.inc'; fi

# Python 3.6.6 pyenv
eval "$(pyenv init -)"

