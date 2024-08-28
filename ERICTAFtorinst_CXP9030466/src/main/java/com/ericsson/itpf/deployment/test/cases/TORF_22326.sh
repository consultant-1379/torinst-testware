#! /bin/sh

TEST_LOGS=/var/log/torinst
DATENOW=$(date +"%Y-%m-%d-%H:%M:%S")
LOGFILE=${TEST_LOGS}/TORF_22326-${DATENOW}.log

for i in `litp /inventory/deployment1 find --resource service-group`
 do
 service_group=$(basename $i)
 echo "Executing command [ssh sc-1 'cmw-repository-list --campaign | xargs cmw-campaign-status' | grep $service_group]" >> ${LOGFILE}
 test=$(ssh sc-1 'cmw-repository-list --campaign | xargs cmw-campaign-status' | grep $service_group)
 if [[ $? -ne 0 ]]; then
	echo "Error running ssh command on sc-1. Either cannot connect to sc-1 or cannot check the status of campaigns" >> ${LOGFILE}
	exit 1
 fi

echo "camp found for $test" >> ${LOGFILE}
 done

exit 0