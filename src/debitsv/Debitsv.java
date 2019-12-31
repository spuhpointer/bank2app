import org.endurox.*;
import java.util.*;

public class Debitsv implements Server, Service {

	final int DEBIT_OFFSET = 1000; //Just append transaction result
	
	/**
	 * Just change the transaction result
	 * (currently).
	 * Afterwards code can be upgraded for distributed transaction processing
	 */
    public void tpService(AtmiCtx ctx, TpSvcInfo svcinfo) {

        ctx.tplogDebug("tpService/DEBIT called");
        TypedUbf ub = (TypedUbf)svcinfo.getData();
        
        //Have some extra space
        ub.tprealloc(ub.Bsizeof()+1024);

        //Print the buffer to stdout
        ub.tplogprintubf(AtmiConst.LOG_DEBUG, "Incoming request:");

        //Resize buffer, to have some more space
        ub.tprealloc(ub.Bused()+1024);
	//Have random sleep

	try {
		Thread.sleep((long)(Math.random() * 30));
	} catch (Exception e) {
		
	}
        	
	//Siulate some failures..
	
	if (Math.random() < 0.1) {
		//Reply OK back
		ctx.tpreturn(AtmiConst.TPFAIL, 0, svcinfo.getData(), 0);
		//must have return!!!!
		//Otherwise:
//		N:NDRX:2:d5d3db3a:30397:7f592c8e4800:001:20191230:101458860:aches_single:aCache.c:0680:exception  [org/endurox/exceptions/UbfBMINVALException] not found!!!!
//N:NDRX:2:d5d3db3a:30397:7f592c8e4800:001:20191230:101458860:xj_ubf_throw:ptions.c:0245:exception  [org/endurox/exceptions/UbfBMINVALException] not found!!!!
		return;
	}
        
        long result = 0;
        if (ub.Bpres(bank.TX_RESULT, 0)) {
        	result = ub.BgetLong(bank.TX_RESULT, 0);
        }
        
        result+=DEBIT_OFFSET;
        
        ub.Bchg(bank.TX_RESULT, 0, result);
        
        ctx.tplogInfo("Debit OK %d", result);
        

        //Reply OK back
        ctx.tpreturn(AtmiConst.TPSUCCESS, 0, svcinfo.getData(), 0);
    }

    public int tpSvrInit(AtmiCtx ctx, String [] argv) {
        ctx.tplogDebug("Into tpSvrInit()");

        ctx.tpadvertise("DEBIT", "Debitsv", this);

        return AtmiConst.SUCCEED;
    }
    
    public void tpSvrDone(AtmiCtx ctx) {
        ctx.tplogDebug("Into tpSvrDone()");
    }

    public static void main(String[] args) {

        AtmiCtx ctx = new AtmiCtx();

        Debitsv server = new Debitsv();

        ctx.tprun(server);
    }
}

