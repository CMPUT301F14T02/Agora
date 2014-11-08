package com.brogrammers.agora.test;

import junit.framework.TestCase;
import android.test.ActivityInstrumentationTestCase2;

import com.brogrammers.agora.data.DeviceUser;
import com.brogrammers.agora.views.MainActivity;

public class SetUsernameTest extends ActivityInstrumentationTestCase2<MainActivity> {

	public SetUsernameTest() {
		super(MainActivity.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testSetUsername() {
		DeviceUser user = DeviceUser.getUser();
		
		user.setUsername("BingsF");
		assertEquals(user.getUsername(), "BingsF");
		user.setUsername("BanJoe");
		assertEquals(user.getUsername(), "BanJoe");
	}
	
}