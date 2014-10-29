package ca.ualberta.app.activity;

import ca.ualberta.app.activity.R;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

//The fragment part is from this web site: http://www.programering.com/a/MjNzIDMwATI.html 2014-Oct-20
public class MainActivity extends FragmentActivity {
	private Fragment[] fragments;
	private FragmentManager fragmentManager;
	private FragmentTransaction fragmentTransaction;
	private RadioGroup bottom_Rg;
	private RadioButton main_Rb, fav_Rb, add_Rb, search_Rb, profile_Rb;
	private int lastCheckedId = R.id.main_menu_button;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		bottom_Rg = (RadioGroup) findViewById(R.id.main_menu);
		main_Rb = (RadioButton) findViewById(R.id.main_menu_button);
		fav_Rb = (RadioButton) findViewById(R.id.fav_menu_button);
		add_Rb = (RadioButton) findViewById(R.id.add_menu_button);
		search_Rb = (RadioButton) findViewById(R.id.search_menu_button);
		profile_Rb = (RadioButton) findViewById(R.id.profile_menu_button);
		fragments = new Fragment[4];

		fragmentManager = getSupportFragmentManager();
		fragments[0] = fragmentManager.findFragmentById(R.id.fragement_main);
		fragments[1] = fragmentManager.findFragmentById(R.id.fragement_fav);
		fragments[2] = fragmentManager.findFragmentById(R.id.fragement_search);
		fragments[3] = fragmentManager.findFragmentById(R.id.fragement_profile);
		fragmentTransaction = fragmentManager.beginTransaction()
				.hide(fragments[0]).hide(fragments[1]).hide(fragments[2])
				.hide(fragments[3]);
		fragmentTransaction.show(fragments[0]).commit();

		setFragmentIndicator();
	}

	private void setFragmentIndicator() {

		bottom_Rg = (RadioGroup) findViewById(R.id.main_menu);
		bottom_Rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				fragmentTransaction = fragmentManager.beginTransaction()
						.hide(fragments[0]).hide(fragments[1])
						.hide(fragments[2]).hide(fragments[3]);
				switch (checkedId) {
				case R.id.main_menu_button:
					fragmentTransaction.show(fragments[0]).commit();
					break;

				case R.id.fav_menu_button:
					fragmentTransaction.show(fragments[1]).commit();
					break;

				case R.id.add_menu_button:
					// go to Create Question activity
					switch (lastCheckedId) {
					case R.id.main_menu_button:
						main_Rb.performClick();
						break;
					case R.id.fav_menu_button:
						fav_Rb.performClick();
						break;
					case R.id.search_menu_button:
						search_Rb.performClick();
						break;
					case R.id.profile_menu_button:
						profile_Rb.performClick();
						break;
					}
					add_Rb.setOnClickListener(new OnClickListener() {
						public void onClick(View v) {
							Intent intent = new Intent(MainActivity.this,
									CreateInputsActivity.class);
							startActivity(intent);
						}
					});
					break;

				case R.id.search_menu_button:
					fragmentTransaction.show(fragments[2]).commit();
					break;

				case R.id.profile_menu_button:
					fragmentTransaction.show(fragments[3]).commit();
					break;

				default:
					break;
				}
				lastCheckedId = checkedId;
			}
		});
	}

}