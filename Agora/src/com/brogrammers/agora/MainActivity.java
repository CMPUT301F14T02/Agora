package com.brogrammers.agora;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
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

public class MainActivity extends Activity {
    private LayoutInflater inflater = (LayoutInflater) Agora.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    private List<Question> results = new ArrayList<Question>();

	private QuestionController qController = QuestionController.getController();
	private QuestionAdapter qAdapter;
   
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //ESDataManager es = ESDataManager.getInstance();
		//try {
		//	results = es.getQuestions();
		//} catch (UnsupportedEncodingException e) {
		//	e.printStackTrace();
		//}

  
		//qController.addQuestion("TITLE BODY END", "BODYBODYBODY", null);
		//ListView lv = (ListView)findViewById(R.id.listView1);
		//qAdapter = new QuestionAdapter(qController);
		//lv.setAdapter(qAdapter);

		
		final CountDownLatch signal = new CountDownLatch(1);
        Long qid = controller.addQuestion("Test Title E", "Test Body E", null);
		try {
			signal.await(2, TimeUnit.SECONDS);
		} catch (InterruptedException e) {

		}
        Log.e("ID", qid.toString()); 
        Long aid = controller.addAnswer("Answer Body E", null, qid);

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
        case R.id.goto_question_answer:
          	Intent intent = new Intent(Agora.getContext(), AnswerActivity.class);
          	startActivity(intent);
        	//Toast.makeText(Agora.getContext(), Integer.toString(results.size()), 0).show();
        default:
            return super.onOptionsItemSelected(item);
        }
    }
      
    
    public void openAddQuestionView() {
    	Intent intent = new Intent(Agora.getContext(), AuthorQuestionActivity.class);
    	//Intent intent = new Intent(Agora.getContext(), QuestionActivity.class); //for opening QuestionActivity
    	
    	startActivity(intent);
    	//Toast.makeText(Agora.getContext(), "Hook up Add a question here", Toast.LENGTH_SHORT).show();
    }
    
    public void openSearchBar(MenuItem item) {
        //SearchView searchView = (SearchView) item.getActionView();
    		//Toast.makeText(Agora.getContext(), "Add Dropdown Search", Toast.LENGTH_SHORT).show();
    		Intent i = new Intent(this, SearchActivity.class);
        startActivity(i);
    }
    
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
    
    
}
