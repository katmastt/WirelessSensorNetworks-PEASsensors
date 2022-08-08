package net.java.dev.netbeansspot.PEAS;
import javax.microedition.io.Datagram;
import java.io.IOException;
import java.util.Random;
import javax.microedition.io.Connector;
import javax.microedition.midlet.MIDlet;
import com.sun.spot.util.Utils;
import javax.microedition.midlet.MIDletStateChangeException;
import javax.microedition.io.DatagramConnection;


public class PEAS extends MIDlet {
public int Node_State=2;
public static int l=2;
Random rand=new Random();
public int B_Radius =rand.nextInt(40)+1;
private final String MAC=new String("0a00.020f.0000.1001");
public int x1= rand.nextInt(100); 
public int x2= rand.nextInt(100); 
public int y1= rand.nextInt(100); 
public int y2= rand.nextInt(100); 
public double Distance=Math.sqrt((x2-x1)^2-(y2-y1)^2);
public int Energy=rand.nextInt(50)+1;
double S_Time;

public void init() {
        
}


private double ln(double y){   
        double xa = 0 ;
        int p=1;
        double ttx;
        double tt = (y-1)/(y+1) ;
        double x1,x2;
        do
        {
            ttx = tt ;
            xa = xa + tt / p;
            x1=(y-1)/(y+1);
            x2= (y-1)/(y+1);
            tt=(tt*(x1*x2));
            p = p + 2 ;
        } while(ttx - tt > 0.0000005 );
        return 2*xa;
        
    }



public void Report_Thread (){
    new Thread() {
            public void run() {
            if(Node_State==0){
                System.out.println("The node is active");
            }    
            else if(Node_State==1){
                System.out.println("The node is sleeping");
            }
            else{
                System.out.println("The node is probing");
            }
            System.out.println("The sleeping time of the node is ");
            System.out.println(S_Time);
            System.out.println("Node id: 0a00.020f.0000.");
            System.out.println(MAC);
            }
        }.start();
    }

  /*  protected void startApp() throws MIDletStateChangeException {
        throw new java.lang.UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    protected void pauseApp() {
        throw new java.lang.UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    protected void destroyApp(boolean bln) throws MIDletStateChangeException {
        throw new java.lang.UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
           
*/

public void Broadcast_Thread(){
    new Thread() {
       // String mes,dg=" ";
       
        public void run() {
            //Node_State=2;
            try{
                if(Distance<=B_Radius){
                    if(Node_State==2){
                        if(Energy!=0){
                            DatagramConnection mes = (DatagramConnection) Connector.open("radiogram//broadcast:10");
                            Datagram dg= mes.newDatagram(mes.getMaximumLength());
                            dg.writeInt(x1);
                            dg.writeInt(y1);
                            dg.writeChars(MAC);
                          
                                   
                            
                            mes.send(dg);
                        }
                    }
                   // else if(Node_State==0){
                       // mes.receive(dg);
   
                  //  }
                }
            }
            catch(IOException ex){
                System.out.println("Error:Broadcast failed");
            }
        }
    }.start();
    int spot=3;
            Energy=Energy-1;
            int pst = 1-rand.nextInt(1)+1;
            int nst= rand.nextInt(1)+1;
            S_Time = Math.abs((1/l) * (ln(pst))*spot - (1/l)* (ln(nst)) ); 
            Utils.sleep((new Double(S_Time)).intValue() * 1000); 
            
            }
            public void pause(){
                Node_State=1;
            }

protected void startApp() throws MIDletStateChangeException {
        new com.sun.spot.service.BootloaderListenerService().getInstance().start();
        init();
        Utils.sleep(5000);
        Broadcast_Thread();
        Report_Thread();
        
    }

    protected void pauseApp() {
        // This is not currently called by the Squawk VM
    }

    /**
     * Called if the MIDlet is terminated by the system. I.e. if startApp throws
     * any exception other than MIDletStateChangeException, if the isolate
     * running the MIDlet is killed with Isolate.exit(), or if VM.stopVM() is
     * called.
     *
     * It is not called if MIDlet.notifyDestroyed() was called.
     *
     * @param unconditional If true when this method is called, the MIDlet must
     * cleanup and release all resources. If false the MIDlet may throw
     * MIDletStateChangeException to indicate it does not want to be destroyed
     * at this time.
     * @throws javax.microedition.midlet.MIDletStateChangeException
     */
    protected void destroyApp(boolean unconditional) throws MIDletStateChangeException {
        // TODO
    }
}      