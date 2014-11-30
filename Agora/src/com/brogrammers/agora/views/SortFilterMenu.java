package com.brogrammers.agora.views;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.Toast;

import com.brogrammers.agora.Agora;
import com.brogrammers.agora.R;
import com.brogrammers.agora.helper.FilterSorterHelper;

public class SortFilterMenu {
	private Activity activity; 
	private QuestionAdapter qAdapter;
	
	public SortFilterMenu(Activity activity, QuestionAdapter qAdapter) {
		this.activity = activity;
		this.qAdapter = qAdapter;
	}
	
	/**
	 * Opens sort dialog where user can filter/sort mainActivity. By favourites,
	 * score, and by picture. Currently does not work. Need to implement.
	 */
	public void openMenu() {
		// Create Dialog Menu for the Sorting Menu
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
				
		// Get the layout inflater;
		// Inflate and set the layout for the dialog
		// Pass null as the parent view because its going in the dialog layout

		final View dialog = ((LayoutInflater) Agora.getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.sortdialogue, null);
		
		final RadioButton rbUpvote = (RadioButton) dialog.findViewById(R.id.sortupvoteradioButton);
		final RadioButton rbDate = (RadioButton) dialog.findViewById(R.id.sortbyDateRadioButton);
		final RadioButton rbAscending = (RadioButton) dialog.findViewById(R.id.arrangebyAscOrderRadio);
		final RadioButton rbDescending = (RadioButton) dialog.findViewById(R.id.descOrderRadioButton);
		final CheckBox checkFavorite = (CheckBox) dialog.findViewById(R.id.favouritecheck);
		final CheckBox checkPicture = (CheckBox) dialog.findViewById(R.id.picturecheck);
		
		rbUpvote.setChecked(FilterSorterHelper.byUpvote);
		rbDescending.setChecked(FilterSorterHelper.descending);
		checkFavorite.setChecked(FilterSorterHelper.filterFavorite);
		checkPicture.setChecked(FilterSorterHelper.filterPicture);
		
		builder.setTitle("Sorting Options");
		builder.setView(dialog)
				// Add action buttons
				.setPositiveButton(R.string.sort,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								FilterSorterHelper.byUpvote = rbUpvote.isChecked();
								FilterSorterHelper.descending = rbDescending.isChecked();
								FilterSorterHelper.filterFavorite = checkFavorite.isChecked();
								FilterSorterHelper.filterPicture = checkPicture.isChecked();
							
								if(rbDate.isChecked()) {
									Toast.makeText(Agora.getContext(), "Sorting by Date", Toast.LENGTH_SHORT).show();
								} else {
									Toast.makeText(Agora.getContext(), "Sorting by Upvote", Toast.LENGTH_SHORT).show();
								}
								
								qAdapter.doSortAndFilter();
							}
				})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
							}
						});

		builder.create();
		builder.show();

	}
}
