package com.brogrammers.agora;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.SearchManager;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.Toast;
/**
 * Main view of the Agora app. Retrieves list of question from 
 * controller and creates a listview.
 * Todo: Add number of answers to questions, implement search/sort.
 * @author Group02
 *
 */
public class MainActivity extends Activity implements Observer {
    private LayoutInflater inflater = (LayoutInflater) Agora.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    private List<Question> results = new ArrayList<Question>();

	private QuestionController qController = QuestionController.getController();
	private QuestionAdapter qAdapter;
   
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        

    }
    /**
     * When resuming mainactivity, retrieve new list of questions and update listview.
     */
    protected void onResume() {
    	super.onResume();
		final CountDownLatch signal = new CountDownLatch(1);

        qController.setObserver(this);
		try {
			signal.await(200, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
		
		}
        List<Question> qList = qController.getAllQuestions();
	    ListView lv = (ListView)findViewById(R.id.listView1);
		qAdapter = new QuestionAdapter(qList, this);

		lv.setAdapter(qAdapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
    		// Dan Global Search Manager? 
        SearchManager searchManager = (SearchManager) getSystemService(Agora.getContext().SEARCH_SERVICE);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int item_id = item.getItemId();
        
        switch (item_id) {
        case R.id.addQuestionBQV:
        	openAddQuestionView();
        	return true;
        case R.id.searchBQV:
        	openSearchBar(item);
        	return true;
        case R.id.sortBQV:
        	openSortMenu();
        	return true;
        case R.id.refreshMain:
        	onResume();
        default:
            return super.onOptionsItemSelected(item);
        }
    }
      
    /**
     * Opens author question view so that user can author question.
     * 
     */
    public void openAddQuestionView() {
    	Intent intent = new Intent(Agora.getContext(), AuthorQuestionActivity.class);
    	//Intent intent = new Intent(Agora.getContext(), QuestionActivity.class); //for opening QuestionActivity
    	
    	startActivity(intent);
    	//Toast.makeText(Agora.getContext(), "Hook up Add a question here", Toast.LENGTH_SHORT).show();
    }
    /**
     * Sends user to search activity, 
     * @param item 
     */
    public void openSearchBar(MenuItem item) {
        //SearchView searchView = (SearchView) item.getActionView();
    		//Toast.makeText(Agora.getContext(), "Add Dropdown Search", Toast.LENGTH_SHORT).show();
    		Intent i = new Intent(this, SearchActivity.class);
        startActivity(i);
    }

    /**
     * Opens sort dialog where user can filter/sort mainActivity.
     * Currently does not work. Need to implement.
     */
    public void openSortMenu() {
        // Create Dialog Menu for the Sorting Menu
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Get the layout inflater;
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
    	builder.setTitle("Sorting Options");
        builder.setView(inflater.inflate(R.layout.sortdialogue, null))
        // Add action buttons
               .setPositiveButton(R.string.sort, new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int id) {
                       // Do something (i.e. pull id from sorting option)
                   }
               })
               .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                   }
               }); 
        
        builder.create();
        builder.show();
    		         	
    }

	@Override
	public void update() {
		qAdapter.notifyDataSetChanged();
//		Toast.makeText(this, "Notifiy qAdapter Change", 0).show();
	}


    
    
}
