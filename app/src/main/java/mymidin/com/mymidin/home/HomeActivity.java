package mymidin.com.mymidin.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import mymidin.com.mymidin.customer.CustomerListFragment;
import mymidin.com.mymidin.product.ProductListFragment;
import mymidin.com.mymidin.R;
import mymidin.com.mymidin.report.ReportActivity;
import mymidin.com.mymidin.sales.SalesListFragment;
import mymidin.com.mymidin.auth.SignInActivity;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    Toolbar toolbar;
    FloatingActionButton addBtn;
    View header;

    SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar =  findViewById(R.id.toolbar);
        toolbar.setTitle("My Midin");
        setSupportActionBar(toolbar);


        addBtn = findViewById(R.id.add_btn); //setting fab button
        addBtn.hide();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView =  findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        header = navigationView.getHeaderView(0); //get navigation header 

        setUserProfile(user); // set user profile in header
        setFragment(new HomeFragment()); //set default fragment 
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment fragment = new HomeFragment();

        switch (id){

            case R.id.nav_home:
                fragment = new HomeFragment();
                addBtn.hide();
                toolbar.setTitle("My Midin");
                break;

            case R.id.nav_sales:
                fragment = new SalesListFragment();
                addBtn.show();
                toolbar.setTitle("Sales");
                break;

            case R.id.nav_customer:
                fragment = new CustomerListFragment();
                addBtn.show();
                toolbar.setTitle("Customer");
                break;

            case R.id.nav_product:
                fragment = new ProductListFragment();
                addBtn.show();
                toolbar.setTitle("Product");
                break;

            case R.id.nav_report:
                addBtn.hide();
                startActivity(new Intent(this,ReportActivity.class));
                break;

            case R.id.nav_sign_out:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this,SignInActivity.class));
                break;

        }

        setFragment(fragment);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //setting user profile in the app
    private void setUserProfile(FirebaseUser user){

        CircleImageView profilePic = header.findViewById(R.id.profile_pic);
        Picasso.get().load(user.getPhotoUrl()).into(profilePic);

        TextView displayName = header.findViewById(R.id.display_name);
        displayName.setText(user.getDisplayName());

        TextView emailText = header.findViewById(R.id.email_text);
        emailText.setText(user.getEmail());

    }


    //depreciate since there is a Fragment Utility
    private void setFragment(Fragment fragment){

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.midin_fragment,fragment);
        transaction.commit();

    }

    public FloatingActionButton getFloatingButton(){

        return addBtn;
    }

}
