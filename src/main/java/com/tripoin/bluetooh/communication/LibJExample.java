package com.tripoin.bluetooh.communication;

import de.serviceflow.codegenj.ObjectManager;
import org.bluez.Adapter1;
import org.bluez.Device1;
import org.bluez.GattCharacteristic1;
import org.bluez.GattService1;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;

/**
 * Created on 12/22/17.
 *
 * @author <a href="mailto:fauzi.knightmaster.achmad@gmail.com">Achmad Fauzi</a>
 */
public class LibJExample {

    public  void start() throws InterruptedException {
        ObjectManager m = ObjectManager.getInstance();
        ObjectManager.getLogger().setLevel(Level.FINE);

        // Show what's on the bus:
        m.dump();

        List<Adapter1> adapters = m.getAdapters();
        System.out.println(" ==> # = " + adapters.size());

        // Find our bloetooth adapter, and start Discovery ...
        Adapter1 defaultAdapter = null;
        for (Adapter1 a : adapters) {
            System.out.println(" ==> Adapter: " + a.getName());
            try {
                a.startDiscovery();
            } catch (IOException e) {
                System.out.println(" ... ignored.");
                continue;
            }
            defaultAdapter = a;
        }
        if (defaultAdapter==null) {
            System.out.println("no useable adapter found. Exit.");
            System.exit(1);
        }

        // Wait for devices to be discovered
        Thread.sleep(5000);

        Adapter1 a = defaultAdapter;
        for (Device1 d : a.getDevices()) {
            if ("CC2650 SensorTag".equals(d.getName())) {
                System.out.println(" ==> Device: " + d.getName());
                try {
                    if (!d.getConnected()) {
                        d.connect();
                        System.out.println(" ... connected.");
                    }
                    else {
                        System.out.println(" ... already connected.");
                    }
                } catch (IOException e) {
                    System.out.println(" ... ignored: "+e.getMessage());
                }
            }
            else {
                System.out.println(" --> Device " + d.getName()+" skipped.");
            }
        }

        // Use the API to traverse through the tree.
        System.out.println("*** Object Tree:");
        for (Adapter1 adp : adapters) {
            System.out.println(" ... adapter "+adp.getObjectPath()+"  "+adp.getName());
            for (Device1 d : a.getDevices()) {
                System.out.println("  .. device "+d.getObjectPath()+"  "+d.getName());
                for (GattService1 s :  d.getServices()) {
                    System.out.println("   . service "+s.getObjectPath()+"  "+s.getUUID());
                    for (GattCharacteristic1 c :  s.getCharacteristics()) {
                        System.out.println("    . char "+c.getObjectPath()+"  "+c.getUUID());
                    }
                }
            }
        }

        try {
            defaultAdapter.stopDiscovery();
            System.out.println(" ... stopped.");
        } catch (IOException e) {
            System.out.println(" ... ignored.");
        }

    }
}
