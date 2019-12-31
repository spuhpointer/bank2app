import org.endurox.*;
import org.endurox.exceptions.AtmiException;
public class Txuploadcl implements Runnable {

	//We received shutdown signal
	@Override
	public void run() {
		running = false;
	}

	/**
	 * Run until shutdown
	 */
    boolean running = true;
    
	/**
     * In the loop call the transaction server with random data
     */
    public void apprun(AtmiCtx ctx) {

    	long amt = 0;
        ctx.tplogDebug("apprun called");

        //Load the data
        while (running) {

        	amt++;
            try {
            	
            	TypedUbf ub = (TypedUbf)ctx.tpalloc("UBF", "", 1024);
            	
            	//Load the data
            	
            	ub.Bchg(bank.TX_AMOUNT, 0, amt);
            	
            	//Say this is original transactions (transfer)
            	ub.Bchg(bank.TX_TYPE, 0, 200);
            	
            	ub.Bchg(bank.TX_SRC_ACCOUNT, 0, "NL84ABNA3496035218");
            	ub.Bchg(bank.TX_DST_ACCOUNT, 0, "PT20003506518965921581172");
            	ub.Bchg(bank.TX_CURRENCY,  0,  "978");
            	ub.Bchg(bank.TX_TEST_FLAG,  0,  0);
            	
            	//Transaction id would timestmap string + sequence number
            	
            	Long ut2 = System.currentTimeMillis();
            	
            	String id = ut2.toString().concat(((Long)amt).toString());
            	
            	ub.Bchg(bank.TX_ID, 0, id);
            	
                ub = (TypedUbf)ctx.tpcall("HANDLER", ub, 0);
                
                
                //Print the result:
                
                short result = ub.BgetShort(bank.TX_RESULT, 0);
                String resultMsg = ub.BgetString(bank.TX_RESULT_MSG, 0);
                
                ctx.tplogInfo("Transaction id: %s result: %d msg: %s", id, result, resultMsg);
                
                //Print the buffer to stdout
                //ub.tplogprintub
                //f(AtmiConst.LOG_DEBUG, "Got response:");
            } catch (AtmiException e) {
                ctx.tplogInfo("got exception: %s", e.toString());
            }
        }
        
    }

    public void appinit(AtmiCtx ctx) {
        ctx.tplogDebug("Into tpSvrInit()");
        
        ctx.installTermSigHandler(this);

    }

    public void unInit(AtmiCtx ctx) {
    	
    	ctx.tplogInfo("Grafeful shutdown");
        ctx.tpterm();
    }

    public static void main(String[] args) {

        AtmiCtx ctx = new AtmiCtx();

        Txuploadcl cl = new Txuploadcl();
        cl.appinit(ctx);
        cl.apprun(ctx);
        cl.unInit(ctx);        
    }
}
