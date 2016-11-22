package net.pocketmagic.keyinjector;

import android.app.Activity;
import android.app.Instrumentation;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.ServiceManager;
import android.os.SystemClock;
import android.util.Log;
import android.view.IWindowManager;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

/* @AndroidKeyInjector
 * 
 * License to access, copy or distribute this file.
 * This file or any portions of it, is Copyright (C) 2012, Radu Motisan . All rights reserved.
 * @author Radu Motisan, radu.motisan@gmail.com
 * @website www.pocketmagic.net
 * 
 * This file is protected by copyright law and international treaties. Unauthorized access, reproduction 
 * or distribution of this file or any portions of it may result in severe civil and criminal penalties.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * 
 * @purpose 
 */


public class AndroidKeyInjectorActivity extends Activity implements OnClickListener {
    
	private Handler handler = null;
	private boolean m_bDoInject = false, m_bRunning = true;
	static int method = 2; // change this to 1,2,3
	
	// method 1
	IBinder wmbinder = ServiceManager.getService( "window" ); 
	IWindowManager m_WndManager = IWindowManager.Stub.asInterface( wmbinder );
	// method 2
	Instrumentation m_Instrumentation = new Instrumentation();
	// method 3: uses jni
	NativeInput m_ni = new NativeInput();
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        LinearLayout panel = new LinearLayout(this);
        panel.setOrientation(LinearLayout.VERTICAL);
        setContentView(panel);
        
        TextView tv = new TextView(this);
        tv.setText("Key Injector Test\n(C)2012 Radu Motisan \nwww.pocketmagic.net\nAll rights reserved.\nMethod:"+method);
        panel.addView(tv);
        
        Button b = new Button(this);
        b.setText("Start");
        b.setOnClickListener((OnClickListener) this);
        b.setId(100);
        panel.addView(b);
        
        b = new Button(this);
        b.setText("Stop");
        b.setOnClickListener((OnClickListener) this);
        b.setId(101);
        panel.addView(b);
        
        EditText et = new EditText(this);
        et.setHint("Tap and select to see Injected keys");
        et.setText("");
        panel.addView(et);
        
        // start
        final Thread t = new Thread() {
            public void run() {
            	while (m_bRunning) {
            		// wait 1sec
            		try { sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }
            		if (!m_bDoInject) continue;
        			Log.d("AKI", "Inject method:"+ method);
        			switch (AndroidKeyInjectorActivity.method) {
            			case 1: {
            				// key down
           			  		m_WndManager.injectKeyEvent( new KeyEvent( KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_A ),false );
           			  		// key up
           			  		m_WndManager.injectKeyEvent( new KeyEvent( KeyEvent.ACTION_UP, KeyEvent.KEYCODE_A ),false );
            			} break;
            			case 2: {
            				m_Instrumentation.sendKeyDownUpSync( KeyEvent.KEYCODE_B );

//                            float posx = 0.5f;
//                            float posy = 0.1f;
//
//                            m_Instrumentation.sendPointerSync(MotionEvent.obtain(
//                                    SystemClock.uptimeMillis(),
//                                    SystemClock.uptimeMillis(),
//                                    MotionEvent.ACTION_DOWN, posx*720, (1-posy)*1022, 0));

                        } break;
            			case 3: {
            				m_ni.SendKey(46, true);
            				m_ni.SendKey(46, false);

            			} break;
        			}
           		}
            }
        };
        t.start();

    }
    

    @Override public void onDestroy() {
		super.onDestroy();
		m_bRunning = false;
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == 100) { //start
			m_bDoInject=true;
		} else if (id == 101) { //stop
			m_bDoInject=false;
		}
	}

    
}