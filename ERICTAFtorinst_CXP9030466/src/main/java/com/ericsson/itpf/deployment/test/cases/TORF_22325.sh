#! /bin/sh

TEST_LOGS=/var/log/torinst
DATENOW=$(date +"%Y-%m-%d-%H:%M:%S")
LOGFILE=${TEST_LOGS}/TORF_22325-${DATENOW}.log
		
		storagepool=$(litp /inventory/deployment1/cluster1/sc1/sc_solution_1/os/nasapi_sc1 show | grep storage_pool | awk -F\" '{print $2}')

            	bladeiparray=()
                storageiparray=()

                for i in `litp /inventory/deployment1/litp_services_pool find --resource ip-address`; do
                	bladeip=$(litp $i show | grep address | awk -F\" '{print $2}')
                    bladeiparray+=("${bladeip}")
                done

				msstorageip=$(litp /inventory/deployment1/ms1/ip_storage show | grep address | awk -F\" '{print $2}')
				  storageiparray[0]=$msstorageip
				 
                for j in `litp /inventory/deployment1/cluster1 find --resource ip-address | grep "\/ip_storage"`; do
                	storageip=$(litp $j show | grep address | awk -F\" '{print $2}')
                  	storageiparray+=("${storageip}")
                done

                counter=0

                for i in ${bladeiparray[@]}
                do
                storageip=${storageiparray[$counter]}
                echo "Storage ip = $storageip" >> ${LOGFILE}
                bladeip=${bladeiparray[$counter]}
                echo "Blade ip = $bladeip" >> ${LOGFILE}
                nfs_shares=$(su - storadm -c 'ssh master@nasconsole nfs share show' | egrep "^/vx/${storagepool}" | grep "${storageip}" | awk '{ print $1 }')
                local_nfs_mounts=$(ssh root@${bladeip} "nfsstat -m" | grep -vi Flags )
                counter=$(( counter + 1 ))

                while read -r nfs_line; do
                if [ ${#nfs_line} -gt 2 ]; then
                        match_found=0
                        while read -r nfsstat_line; do
                                if [ ${#nfsstat_line} -gt 2 ]; then
                                        nfsstat_fs=${nfsstat_line#*:}
                                        if [ $nfsstat_fs == $nfs_line ]; then
                                                match_found=1
                                                local_mount=`echo "$nfsstat_line" | awk '{ print $1 }'`
                                                break
                                        fi
                                fi
                        done <<< "$local_nfs_mounts"

                        if [ $match_found -eq 1 ]; then
                                echo "Verified that SFS NFS share $nfs_line is mounted on $local_mount on node ${bladeip}" >> ${LOGFILE}
                        else
                                echo "Did not find SFS NFS share $nfs_line mounted on node ${bladeip}. Please refer to '${LOGFILE}' for further information" >> ${LOGFILE}
                                exit 1
                        fi
                fi
        done <<< "$nfs_shares"

                echo "Verified that all NFS shares on the SFS for node ${bladeip} are mounted on node ${bladeip}" >> ${LOGFILE}

                done

                exit 0

		
	