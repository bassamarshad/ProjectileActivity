package com.example.arshadba;





import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {
	
	public static final float GRAV_CONST=(float) 9.80665;
	
	EditText et1, et2,et3;
	int delayD=0;
	
	TextView tv1,tv2;
	Button button1,button2;
	Thread t1;
	boolean isRunning=true;
	ProgressBar progressbar1;
	
	Handler myHandler = new Handler() {
		@Override
    	public void handleMessage(Message msg) {
    		// TODO Auto-generated method stub
    		//super.handleMessage(msg);
			
		//	progressbar1 = (ProgressBar) findViewById(R.id.progressBar1);
			//progressbar1.setMax(10000);
    		if(msg.what == 101)
    		{
    			tv2.setText(msg.obj.toString());
    		}
    		
    		if(msg.what == 102)
    		{
    			//progressbar1.incrementProgressBy(1);

				// Reset if not done yet
				//if (progressbar1.getProgress() == 10000) {
					//progressbar1.setProgress(0);}
				
    			tv1.setText(msg.obj.toString());
    		}
    		
    		if(msg.what == 103)
    		{
    			//button1.setEnabled(true);
    			//button1.setVisibility(View.VISIBLE);
    		
    			//Previously the below was in the run() method of the runnable  and app was crashing ... we should never update GUI elements
    			//on the main thread from the worker threads.
    		    button2.setVisibility(View.GONE);
    		}
    		
    		
    	}
    };
    
   Runnable myRun =new Runnable() 
    {

	   @Override
		public void run() {
			// TODO Auto-generated method stub
		     
			
	    	
	    	
	    	
	    	float timeImp=(float) 0.0;

		       float vel = Float.parseFloat(et1.getText().toString()) ;
		        float angle = Float.parseFloat(et2.getText().toString()) ;
		       // long delayD = Integer.parseInt(et3.getText().toString());
		        
		        
		        timeImp = (float) (2*vel*(Math.sin(Math.toRadians(angle)))/GRAV_CONST);
		        
		       // Toast.makeText(getApplicationContext(), "timeImp", Toast.LENGTH_SHORT).show();
		        
		        float[] timeArray = new float[10001];
		        
		        int timeCurrent =1;
		        
		        float deltaT=timeImp/10000;
		       // for (int timeCurrent=1;timeCurrent<10001 ;timeCurrent++)
		      // this was the issue ... This thing will only work in a while loop .... (Figured it !!!! )
		        while(timeCurrent < 10001)
		        {
		        	if (isRunning)
					{
		        	//both the below approaches work !!
		        	//timeArray[timeCurrent]=timeCurrent*(deltaT);
		        	timeArray[timeCurrent]=deltaT +timeArray[timeCurrent-1];
		        	
			float xt =  (float) (vel*(Math.cos(Math.toRadians(angle)))*timeArray[timeCurrent]);
			float yt =	 (float) (vel*(Math.sin(Math.toRadians(angle))) - (((GRAV_CONST)* ((timeArray[timeCurrent])*(timeArray[timeCurrent])))/2));
			//to handle the negative value of y-coordinate
			if (yt < 0)
			{
				yt=(float) 0.0;
			}
			Message msg1 = myHandler.obtainMessage(101, "( "+xt+" ,"+yt+" )");
			Message msg2 = myHandler.obtainMessage(102, timeArray[timeCurrent]);
			myHandler.sendMessage(msg1);
			myHandler.sendMessage(msg2);
			try {
				//Reduce to 1ms or increase as you want to improve the visibility on the display 
				Thread.sleep(delayD);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//below handles my button in the Handler
			if(timeCurrent==10000)
			{
				Message msg3 = myHandler.obtainMessage(103);
				myHandler.sendMessage(msg3);
			}

		        } //if
		        	else
					{
						continue;
					}
		        	
		        	
		        	timeCurrent++;    	
		}//for
		        
		        
		} //run function
		
    }; //Runnable
		        
		        
		
		
	
	

    
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
      tv1 =(TextView) findViewById(R.id.textView5);
        tv2 =(TextView) findViewById(R.id.textView7);
        
        tv1.setTextColor(Color.GREEN);
        tv2.setTextColor(Color.GREEN);
        tv1.setBackgroundColor(Color.BLACK);
        tv2.setBackgroundColor(Color.BLACK);
        button2 = (Button) findViewById(R.id.button2);
 	    button2.setText("Click to Pause");
  	    button2.setVisibility(View.GONE);
     
       /* button2 = (Button) findViewById(R.id.button2);
        button2.setText("Click to Interrupt the Thread");*/
  	    
  	  SeekBar seekBar = (SeekBar)findViewById(R.id.seekbar); 
     final TextView seekBarValue = (TextView)findViewById(R.id.seekbarvalue); 

      seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){ 

  @Override 
  public void onProgressChanged(SeekBar seekBar, int progress, 
    boolean fromUser) { 
   // TODO Auto-generated method stub 
   seekBarValue.setText(String.valueOf(progress)); 
	  delayD = progress;
	  
  } 

  @Override 
  public void onStartTrackingTouch(SeekBar seekBar) { 
   // TODO Auto-generated method stub 
  } 

  @Override 
  public void onStopTrackingTouch(SeekBar seekBar) { 
   // TODO Auto-generated method stub 
  } 
      }); 
        
        
        
        
    }


   


	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    
    public void startCalc(View v)
    {
    	//Disable button
		button1 = (Button) findViewById(R.id.button1);
    	button1.setEnabled(false);
    	button1.setVisibility(View.GONE);
    	
    	 button2 = (Button) findViewById(R.id.button2);
	    	button2.setVisibility(View.VISIBLE);
    	
    	
    	
    	    et1 =(EditText) findViewById(R.id.editText1);
	        et2 =(EditText) findViewById(R.id.editText2);
	        et3 = (EditText) findViewById(R.id.editText3);
	        
	      //Below to automatically hide the keyboard when processing is in place,,,,
	    	InputMethodManager inputMgr = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
	    	inputMgr.hideSoftInputFromWindow(et1.getWindowToken(), 0);
	    	inputMgr.hideSoftInputFromWindow(et2.getWindowToken(), 0);
	    	inputMgr.hideSoftInputFromWindow(et3.getWindowToken(), 0);
	        
	        
	        
	       // if(et1.getText().toString().equals(null) || et1.getText().toString().equals("") || et1.getText().toString().equals(null) || et1.getText().toString().equals("") || et2.getText().toString().equals(null) || et2.getText().toString().equals("") || et3.getText().toString().equals(null) || et3.getText().toString().equals(""))
	        if(et1.getText().toString().equals(null) || et1.getText().toString().equals("") || et1.getText().toString().equals(null) || et1.getText().toString().equals("") || et2.getText().toString().equals(null) || et2.getText().toString().equals("") )
	  	      
	    	{
	            Toast.makeText(getApplicationContext(),"Please Enter Proper values in the Edit Boxes",Toast.LENGTH_LONG).show(); 
	            button1.setEnabled(true);
	            button2.setVisibility(View.GONE);
	            return;
	            
	        }
	        
	        
	        
    	
    	 t1 = new Thread (myRun); 
    	t1.start();
    	
    	
   }
    
   public void pauseMe(View v)
   {
	   
	  Button button2 = (Button) findViewById(R.id.button2);
	  
       if(t1.isAlive())
       {
	   isRunning = !isRunning;
	   if(isRunning)
	   {
		   button2.setText("Click to Pause");
	   }
	   else{
		   button2.setText("Click to Resume");   
	   }
	   
	     // Toast.makeText(getApplicationContext(),String.valueOf(isRunning),Toast.LENGTH_LONG).show(); 
                    
       }
	      
                
   }
   
   public void startAgain(View v)
   {
	   // Got Below code from StackOverflow to "restart" my app
	   //http://stackoverflow.com/questions/2470870/force-application-to-restart-on-first-activity
	 /*  Intent i = getBaseContext().getPackageManager()
	             .getLaunchIntentForPackage( getBaseContext().getPackageName() );
	i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	startActivity(i);*/
	finish();
	startActivity(new Intent(this, MainActivity.class));
	
   }
    
    
    
    
    
   
}

