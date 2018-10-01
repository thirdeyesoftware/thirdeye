export PS1="\[\033[36m\]\u\[\033[m\]@\[\033[32m\]\h:\[\033[33;1m\]\w\[\033[m\]\$ "
alias ll='ls -GFlha'
alias ls='ls -GFh'
alias vim='/usr/local/bin/vim'
alias vi='/usr/local/bin/vim'
#export JAVA_HOME=/System/Library/Java/JavaVirtualMachines/1.6.0.jdk/Contents/Home
export PATH=$PATH:/usr/local/mysql/bin
COPYFILE_DISABLE=1
export COPYFILE_DISABLE
#export https_proxy=http://thd-corp-proxy.homedepot.com
alias gcp='git cherry-pick'

export GOOGLE_APPLICATION_CREDENTIALS=/Users/jblau/.config/gcloud/application_default_credentials.json
# The next line updates PATH for the Google Cloud SDK.
if [ -f '/private/tmp/google-cloud-sdk/path.bash.inc' ]; then source '/private/tmp/google-cloud-sdk/path.bash.inc'; fi

# The next line enables shell command completion for gcloud.
if [ -f '/private/tmp/google-cloud-sdk/completion.bash.inc' ]; then source '/private/tmp/google-cloud-sdk/completion.bash.inc'; fi
