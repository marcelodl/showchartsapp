package teste.embedded.showchartsapp;

import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;


public class MainActivity extends ActionBarActivity implements ActionBar.TabListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setHomeButtonEnabled(false);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // When swiping between different app sections, select the corresponding tab.
                // We can also use ActionBar.Tab#select() to do this if we have a reference to the
                // Tab.
                actionBar.setSelectedNavigationItem(position);
            }
        });

        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by the adapter.
            // Also specify this Activity object, which implements the TabListener interface, as the
            // listener for when this tab is selected
                    actionBar.addTab(actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

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
            switch (position) {
                case 0:
                    return new BarChartFragment();
                case 1:
                    return new LineChartFragment();
                default:
                    return PlaceholderFragment.newInstance(position + 1);

            }

        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section3).toUpperCase(l);
            }
            return null;
        }
    }

    /**
     * A dummy fragment representing a section of the app, but that simply displays dummy text.
     */
    public static class BarChartFragment extends Fragment {

        public static final String ARG_SECTION_NUMBER = "section_number";

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            try {
                View rootView = inflater.inflate(R.layout.fragment_chart_1, container, false);

                HorizontalBarChart chart = (HorizontalBarChart) rootView.findViewById(R.id.bar_chart);
                chart.setDrawGridBackground(false);

                ArrayList<String> eixoX = new ArrayList<String>();
                ArrayList<BarEntry> eixoY = new ArrayList<BarEntry>();

                String url = "http://teste.acessetecnologia.com.br:3000/teste/all";
                Bebida[] result = new GetData().execute(url).get();

                for (int i = 0; i < result.length; i++) {
                    eixoX.add(result[i].getBebida());
                    eixoY.add(new BarEntry(result[i].getConsumo(), i));
                }
                BarDataSet dataset = new BarDataSet(eixoY, "Bebidas");
                dataset.setColors(ColorTemplate.COLORFUL_COLORS);
                BarData data = new BarData(eixoX, dataset);

                chart.setData(data);

                XAxis xAxis = chart.getXAxis();
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                xAxis.setDrawAxisLine(false);
                xAxis.setDrawGridLines(false);

                YAxis leftYAxis = chart.getAxisLeft();
                leftYAxis.setDrawAxisLine(false);
                leftYAxis.setDrawGridLines(false);

                chart.getAxisRight().setEnabled(false);



                return rootView;

            } catch (InterruptedException e) {
                e.printStackTrace();
                return null;
            } catch (ExecutionException e) {
                e.printStackTrace();
                return null;
            }

        }
    }

    /**
     * A dummy fragment representing a section of the app, but that simply displays dummy text.
     */
    public static class LineChartFragment extends Fragment {

        public static final String ARG_SECTION_NUMBER = "section_number";

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            try {
                View rootView = inflater.inflate(R.layout.fragment_chart_2, container, false);

                LineChart chart = (LineChart) rootView.findViewById(R.id.line_chart);
                chart.setDrawGridBackground(false);

                ArrayList<String> eixoX = new ArrayList<String>();
                ArrayList<Entry> eixoY = new ArrayList<Entry>();

                String url = "http://teste.acessetecnologia.com.br:3000/teste/all";
                Bebida[] result = new GetData().execute(url).get();

                for (int i = 0; i < result.length; i++) {
                    eixoX.add(result[i].getBebida());
                    eixoY.add(new Entry(result[i].getConsumo(), i));
                }
                LineDataSet dataset = new LineDataSet(eixoY,"Bebidas");
                dataset.setColors(ColorTemplate.VORDIPLOM_COLORS);
                LineData data = new LineData(eixoX, dataset);

                chart.setData(data);


                return rootView;

            } catch (InterruptedException e) {
                e.printStackTrace();
                return null;
            } catch (ExecutionException e) {
                e.printStackTrace();
                return null;
            }

        }
    }

    public static class GetData extends AsyncTask<String,Integer,Bebida[]> {

        @Override
        protected Bebida[] doInBackground(String... params) {

            // The connection URL
            String url = params[0];

// Create a new RestTemplate instance
            RestTemplate restTemplate = new RestTemplate();

// Add the String message converter¡
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
// Make the HTTP GET request, marshaling the response to a String
            ResponseEntity<Bebida[]> result = restTemplate.getForEntity(url, Bebida[].class);

            return result.getBody();
        }
    }
    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

}
