package it.pdp2021.ctnode;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class Main {

  public static final int LISTENER_PORT = Integer.parseInt(System.getenv().get("LISTENERPORT"));
  public static final int TARGET_PORT = Integer.parseInt(System.getenv("TARGETPORT"));
  public static final String HQ_ADDRESS = System.getenv("HQADDRESS");
  public static final String TARGET_ADDRESS = System.getenv("TARGETADDRESS");
  public static final int SEQUENCE = Integer.parseInt(System.getenv("SEQUENCE"));
  public static boolean send = "1".equals(System.getenv().get("SENDER"));

  public static void main(String[] args) {

    InetAddress local = readNetworks();
    BlockingQueue<String> received = new LinkedBlockingDeque<>();

    Receiver receiver = new Receiver(local, received, LISTENER_PORT);
    Store store = new Store(received);
    Beacon beacon = new Beacon(TARGET_ADDRESS, TARGET_PORT, SEQUENCE);
    Reader reader = new Reader(store, HQ_ADDRESS);

    new Thread(receiver).start();
    new Thread(store).start();
    new Thread(reader).start();
    if (send) new Thread(beacon).start();

    new Thread(
            () -> {
              try {
                Thread.sleep(40000);
                System.out.println("Closing...");
                receiver.done = true;
                store.done = true;
                reader.done = true;
                beacon.done = true;
              } catch (InterruptedException e) {
                e.printStackTrace();
              }
            })
        .start();
  }

  private static InetAddress readNetworks() {
    Enumeration<NetworkInterface> ifs;
    InetAddress local = null;
    try {
      ifs = NetworkInterface.getNetworkInterfaces();
      while (ifs.hasMoreElements()) {
        NetworkInterface iface = ifs.nextElement();
        Enumeration<InetAddress> addrs = iface.getInetAddresses();
        while (addrs.hasMoreElements()) {
          InetAddress addr = addrs.nextElement();
          if (!addr.isLoopbackAddress()) {
            System.out.println(
                iface.getDisplayName() + " " + addr.toString() + " " + LISTENER_PORT);
            local = addr;
          }
        }
      }
    } catch (SocketException e) {
      e.printStackTrace();
    }
    System.out.println("Target: " + TARGET_ADDRESS + " " + TARGET_PORT);
    return local;
  }
}
