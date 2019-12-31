#include <string.h>
#include <stdio.h>
#include <stdlib.h>
#include <memory.h>
#include <math.h>

#include <ndebug.h>
#include <atmi.h>


#include <ubf.h>
#include <Exfields.h>
#include <bank.fd.h>
/*---------------------------Externs------------------------------------*/
/*---------------------------Macros-------------------------------------*/

#ifndef SUCCEED
#define SUCCEED			0
#endif

#ifndef	FAIL
#define FAIL			-1
#endif

/*---------------------------Enums--------------------------------------*/
/*---------------------------Typedefs-----------------------------------*/
/*---------------------------Globals------------------------------------*/
/*---------------------------Statics------------------------------------*/
/*---------------------------Prototypes---------------------------------*/


/**
 * Service entry
 * @return SUCCEED/FAIL
 */
void HANDLER (TPSVCINFO *p_svc)
{
	int ret = SUCCEED;
	long len;

	UBFH *p_ub = (UBFH *)p_svc->data;
	
	/*realloc for some space*/
	p_ub = (UBFH *)tprealloc((char *)p_ub, 2048);
	
	if (NULL==p_ub)
	{
		TP_LOG(log_error, "Failed to realloc: %s", tpstrerror(tperrno));
		ret=FAIL;
		goto out;
	}

	tplogprintubf(log_info, "Got request", p_ub);
	
	
	if (SUCCEED!=tpcall("DEBIT", (char *)p_ub, 0, (char **)&p_ub, &len, 0))
	{
		TP_LOG(log_error, "Failed to call DEBIT: %s", tpstrerror(tperrno));
		
		Bchg(p_ub, TX_RESULT_MSG, 0, "Failed to call DEBIT", 0);
		
		ret=FAIL;
		goto out;
	}
	
	if (SUCCEED!=tpcall("CREDIT", (char *)p_ub, 0, (char **)&p_ub, &len, 0))
	{
		TP_LOG(log_error, "Failed to call CREDIT: %s", tpstrerror(tperrno));
		Bchg(p_ub, TX_RESULT_MSG, 0, "Failed to call CREDIT", 0);
		ret=FAIL;
		goto out;
	}
	
	Bchg(p_ub, TX_RESULT_MSG, 0, "Transfer OK", 0);

out:

	tpreturn(  ret==SUCCEED?TPSUCCESS:TPFAIL,
		0L,
		(char *)p_ub,
		0L,
		0L);

}

/**
 * Initialize the application
 * @param argc	argument count
 * @param argv	argument values
 * @return SUCCEED/FAIL
 */
int init(int argc, char** argv)
{
	int ret = SUCCEED;

	TP_LOG(log_info, "Initialising...");

	if (SUCCEED!=tpinit(NULL))
	{
		TP_LOG(log_error, "Failed to Initialise: %s", 
			tpstrerror(tperrno));
		ret = FAIL;
		goto out;
	}
	
	/* Advertise our service */
	if (SUCCEED!=tpadvertise("HANDLER", HANDLER))
	{
		TP_LOG(log_error, "Failed to initialise HANDLER!");
		ret=FAIL;
		goto out;
	}	

out:

	
	return ret;
}

/**
 * Terminate the application
 */
void uninit(void)
{
	TP_LOG(log_info, "Uninitialising...");
}

/**
 * Server program main entry
 * @param argc	argument count
 * @param argv	argument values
 * @return SUCCEED/FAIL
 */
int main(int argc, char** argv)
{
	/* Launch the Enduro/x thread */
	return ndrx_main_integra(argc, argv, init, uninit, 0);
}

