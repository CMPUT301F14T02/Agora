package com.brogrammers.agora;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
        	openSearchDropDown();
        	return true;
        case R.id.sortBQV:
        	openSortContextMenu();
        	return true;
        	
        default:
            return super.onOptionsItemSelected(item);
        }
    }
        
    public void openAddQuestionView() {
        // 1. Switch views for add a Question
    	Toast.makeText(this, "Adding question!", Toast.LENGTH_SHORT).show();
    	Intent intent = new Intent(MainActivity.this, AuthorQuestionActivity.class);
		startActivity(intent);
    }
    
    public void openSearchDropDown() {
        // 2. Create a dropdown menu for the search
    	Toast.makeText(this, "Add Dropdown Search", Toast.LENGTH_SHORT).show();
    }
    
    public void openSortContextMenu() {
        // 2. Create a dropdown menu for the search
    	Toast.makeText(this, "Sort Context", Toast.LENGTH_SHORT).show();
    }
    
    

    // 3. Create a context menu for sort 
    
}
