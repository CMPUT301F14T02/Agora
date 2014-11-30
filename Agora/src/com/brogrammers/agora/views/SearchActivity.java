package com.brogrammers.agora.views;

import java.util.List;
import java.util.Locale;

import com.brogrammers.agora.Observer;
import com.brogrammers.agora.R;
import com.brogrammers.agora.R.id;
import com.brogrammers.agora.R.layout;
import com.brogrammers.agora.R.menu;
import com.brogrammers.agora.R.string;
import com.brogrammers.agora.data.QuestionController;
import com.brogrammers.agora.model.Answer;
import com.brogrammers.agora.model.Question;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v13.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
/**
 * Activity(View) to display search results
 * Contains two fragments: one to show answers and one the show answers
 */

public class SearchActivity extends Activity implements ActionBar.TabListener {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a {@link FragmentPagerAdapter}
	 * derivative, which will keep every loaded fragment in memory. If this
	 * becomes too memory intensive, it may be best to switch to a
	 * {@link android.support.v13.app.FragmentStatePagerAdapter}.
	 */
	private SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	private ViewPager mViewPager;
	private SearchView mSearchView;
	private static QuestionController controller = QuestionController.getController();

	private List<Answer> aList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);

		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the activity.
		mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search, menu);
		final SearchView sView = (SearchView)menu.findItem(R.id.searchBQV).getActionView();
		sView.setSubmitButtonEnabled(true);
		sView.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
		sView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
			public boolean onQueryTextChange(String newText) {
				return false;
			}
			public boolean onQueryTextSubmit(String query) {
				int item = (mViewPager.getCurrentItem());
				if (item == 0) {
					((QuestionsFragment)mSectionsPagerAdapter.getItem(item)).doSearch(query);
					Log.wtf("SEARCH", "EXECUTE SERACH CLICKED");
				} else if (item == 1) {
					((AnswersFragment)mSectionsPagerAdapter.getItem(item)).doSearch(query);
				}
				return true;
			}
		});
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
		if (id == R.id.searchBQV) {
			openSearchBar(item);
		}
		return super.onOptionsItemSelected(item);
	}

	private void openSearchBar(MenuItem item) {
		SearchView mSearchView = (SearchView) item.getActionView();
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}


	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a PlaceholderFragment (defined as a static inner class
			// below).
			switch (position) {
			case 0:
				return QuestionsFragment.newInstance(position + 1, SearchActivity.this);
			case 1:
				return AnswersFragment.newInstance(position + 1, SearchActivity.this);
			}
			return null;
		}

		@Override
		public int getCount() {
			// Show 2 total pages.
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.searchtab_questions).toUpperCase(l);
			case 1:
				return getString(R.string.searchtab_answers).toUpperCase(l);
			}
			return null;
		}
	}

	/**
	 * A fragment containing a list of Questions from the search results
	 */
	 public static class QuestionsFragment extends Fragment implements Observer {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";
		private List<Question> qList;
		private QuestionAdapter qAdapter;
		private View rootView;
		private static ListView lv; 
		private static Activity activity;
		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static QuestionsFragment newInstance(int sectionNumber, Activity activity) {
			QuestionsFragment fragment = new QuestionsFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			QuestionsFragment.activity = activity;
			return fragment;
		}

		public QuestionsFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			rootView = inflater.inflate(R.layout.fragment_search_questions,
					container, false);

			lv = (ListView)rootView.findViewById(R.id.qSearchListView);
			if (lv == null) {Log.wtf("SEARCH", "lv set to null");}
			return rootView;
		}
		
		public void update() {
			
			qAdapter = new QuestionAdapter(qList, activity);
			if (qAdapter == null) Log.wtf("SEARCH", "null qAdapter");
			if (lv == null) Log.wtf("SERRCH", "lv null in update");
			lv.setAdapter(qAdapter);
		}
		
		public void doSearch(String query) {
			controller.setObserver(this);
			qList = controller.searchQuestions(query);
		}
	}
	 
	 	/**
		 * A fragment containing a list of Questions from the search results
		 */
		 public static class AnswersFragment extends Fragment implements Observer {
			/**
			 * The fragment argument representing the section number for this
			 * fragment.
			 */
			private static final String ARG_SECTION_NUMBER = "section_number";
			private List<Answer> aList;
			private AnswerAdapter aAdapter;
			private View rootView;
			private static Activity activity;
			
			/**
			 * Returns a new instance of this fragment for the given section number.
			 */
			public static AnswersFragment newInstance(int sectionNumber, Activity activity) {
				AnswersFragment fragment = new AnswersFragment();
				Bundle args = new Bundle();
				args.putInt(ARG_SECTION_NUMBER, sectionNumber);
				fragment.setArguments(args);
				AnswersFragment.activity = activity;
				return fragment;
			}

			public AnswersFragment() {
			}

			@Override
			public View onCreateView(LayoutInflater inflater, ViewGroup container,
					Bundle savedInstanceState) {
				View rootView = inflater.inflate(R.layout.fragment_search_answers,
						container, false);
				return rootView;
			}
			
			public void update() {
				ListView lv = (ListView)this.rootView.findViewById(R.id.qSearchListView);
//				aAdapter = new AnswerAdapter(aList, activity);
//				lv.setAdapter(aAdapter);
			}
			
			public void doSearch(String query) {
				controller.setObserver(this);
//				aList = controller.searchAnswers(query);
			}
		 }	
	 
}
