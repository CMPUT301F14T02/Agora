package com.brogrammers.agora.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import com.brogrammers.agora.DeviceUser;
import com.brogrammers.agora.Question;

import junit.framework.TestCase;

public class SortLocalTest extends TestCase {
	
	private class TestDeviceUser extends DeviceUser {
		public TestDeviceUser() {
			setUsername("TestBingsF");
			favoritesPrefFileName = "TEST_FAVORITES";
			cachedPrefFileName = "TEST_CACHED";
			authoredPrefFileName = "TEST_AUTHORED";
			usernamePrefFileName = "TEST_USERNAME";
		}
	}
	
	public void testSorting() throws InterruptedException {
		final CountDownLatch signal = new CountDownLatch(1);
		DeviceUser user = new TestDeviceUser();
		Question q0 = new Question("q0","b0",null,user);
		Question q1 = new Question("q1","b1",null,user);
		Question q2 = new Question("q2","b2",null,user);
		
		Long id0 = q0.getID();
		Long id1 = q1.getID();
		Long id2 = q2.getID();
		
		ArrayList<Question> questions = new ArrayList<Question>();
		questions.add(q0);
		questions.add(q1);
		questions.add(q2);
		
		//manually set date so fourth is oldest and first is now newest
		questions.get(2).setDate(System.currentTimeMillis());
		signal.await(100, TimeUnit.MILLISECONDS);
		questions.get(1).setDate(System.currentTimeMillis());
		signal.await(100, TimeUnit.MILLISECONDS);
		questions.get(0).setDate(System.currentTimeMillis());
		
		Collections.sort(questions, new Comparator<Question>() {
		    public int compare(Question m1, Question m2) {
		        return m1.getDate().compareTo(m2.getDate());
		    }
		});
		
		//order should now be { firstDate, secondDate, thirdDate, fourthDate}
		assertTrue("Correct count", questions.size() == 3);
		assertTrue("firstDate not first", questions.get(2).getID() == id0);
		assertTrue("secondDate not second", questions.get(1).getID().equals(id1));
		assertTrue("thirdDate not third", questions.get(0).getID() == id2);
		
		
	}
}
