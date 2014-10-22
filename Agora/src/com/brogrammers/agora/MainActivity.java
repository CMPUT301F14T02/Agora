package com.brogrammers.agora;

import android.app.ActionBar;
import android.app.Activity;
import android.app.SearchManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
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
        	
        default:
            return super.onOptionsItemSelected(item);
        }
    }
        
    public void openAddQuestionView() {
    	Intent intent = new Intent(Agora.getContext(), AuthorQuestionActivity.class);
    	startActivity(intent);
    	//Toast.makeText(Agora.getContext(), "Hook up Add a question here", Toast.LENGTH_SHORT).show();
    }
    
    public void openSearchBar(MenuItem item) {
        SearchView searchView = (SearchView) item.getActionView();
    	Toast.makeText(Agora.getContext(), "Add Dropdown Search", Toast.LENGTH_SHORT).show();
    }
    
    public void openSortMenu() {
        //new Dialog 
    	Toast.makeText(Agora.getContext(), "Sort Context", Toast.LENGTH_SHORT).show();
    }
    
    

    
}
