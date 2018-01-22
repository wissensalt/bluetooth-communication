package com.tripoin.bluetooh.communication;

import javax.bluetooth.*;
import java.util.ArrayList;

/**
 * Created on 12/19/17.
 *
 * @author <a href="mailto:fauzi.knightmaster.achmad@gmail.com">Achmad Fauzi</a>
 */
public class SearchServiceImpl implements ISearchService {

    private static final Object lock=new Object();

    public ArrayList<RemoteDevice> devices = new ArrayList<RemoteDevice>();

    private LocalDevice localDevice;

    private BluetoothDiscoveryListener bluetoothDiscoveryListener;

    @Override
    public void searchDevices() {
        try{
            bluetoothDiscoveryListener = new BluetoothDiscoveryListener(lock, devices);
            // 1
            localDevice = LocalDevice.getLocalDevice();
            // 2
            DiscoveryAgent agent = localDevice.getDiscoveryAgent();
            // 3
            agent.startInquiry(DiscoveryAgent.GIAC, bluetoothDiscoveryListener);
            try {
                synchronized(lock){
                    lock.wait();
                }
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Device Inquiry Completed. ");
            searchServices();
        }
        catch (Exception e) {
            System.out.println("Error : "+e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void searchServices() {
        System.out.println("1");
        UUID[] uuidSet = new UUID[1];
        uuidSet[0]=new UUID(0x1105); //OBEX Object Push service

        int[] attrIDs =  new int[] {
                0x0100 // Service name
        };

        LocalDevice localDevice = null;
        try {
            localDevice = LocalDevice.getLocalDevice();
            System.out.println("2");
        } catch (BluetoothStateException e) {
            e.printStackTrace();
        }
        System.out.println("3");
        DiscoveryAgent agent = localDevice.getDiscoveryAgent();
        System.out.println("COnnected devices "+devices.size());

        for (RemoteDevice device : devices) {
            System.out.println("4 "+device.getBluetoothAddress());




            try {
                agent.selectService(uuidSet[0], 1, true);
                agent.searchServices(attrIDs,uuidSet,device, bluetoothDiscoveryListener);
                System.out.println("5");
            } catch (BluetoothStateException e) {
                e.printStackTrace();
            }
            try {
                synchronized(lock){
                    System.out.println("6");
                    lock.wait();
                }
            }
            catch (InterruptedException e) {
                e.printStackTrace();
                return;
            }
            System.out.println("Service search finished.");
        }

    }
}
