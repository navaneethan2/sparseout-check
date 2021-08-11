#!/bin/bash
#This script list all the commits on the modules  in previous 24 hours
#for those modules listed in the 'modules'

source ~/.bashrc

function parse_api_header {
  api_header=`curl -u user:${tfsApiToken} -I -s $1`
}

githubApiToken=$1
branchToCheck=$2
reposListTextFile=$3
# Option 1 - using api to iterate , if api is not availble -> option 2
# option 2 - checkout the complete repo and iterate through the modules and check for the commits.
# option 2 will consume total checkouts.


echo "" > commits-last-night

dayOfWeek=`date +%A`

currentTime=$(date +%H:%M)

# This if else block is in-progress , need to check the weekday / weekend for the commits , if its take 24 hours then weekend code need
#to update on monday build.

if [[ "$currentTime" < "18:00" ]]; then
     case ${dayOfWeek} in
	    "Monday")
		    sinceDateTime=`date --date='4 day ago' +%Y-%m-%d | sed 's/$/T18:00:00Z/'`
		    ;;
	    "Sunday")
		    sinceDateTime=`date --date='3 day ago' +%Y-%m-%d | sed 's/$/T18:00:00Z/'`
		    ;;
	    *)
		    sinceDateTime=`date --date='2 day ago' +%Y-%m-%d | sed 's/$/T18:00:00Z/'`
		    ;;
     esac
fi

echo "Starting  commits script"
echo "Branch to check: ${branchToCheck}"
echo "Current Time: ${currentTime}"
echo "Checking for commits since: ${sinceDateTime}"
echo "Repositories to check: ${repos}"
echo ""

for module in `echo ${modules}`{
do

  http_response_code= "TFS url check for branch to get status code 200"

  if [ "${http_response_code}" = "200" ];
    then
      #based on the time we need to iterate through the modules

  fi
done


}