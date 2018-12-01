package com.example.android.fittracker;


import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChartFragment extends Fragment {

    private FitChart fitChart;

    public ChartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chart, container, false);
        ImageView graphView = view.findViewById(R.id.graph);

        Bundle arguments = getArguments();
        ArrayList<String> dayList;
        ArrayList<String> durationList;
        ArrayList<String> calorieList;
        String chartData = "";
        String chartLabel = "";
        String a = "",b = "",c = "",d = "",e = "",f = "",g = "";
        String daya = "",dayb = "",dayc = "",dayd = "",daye ="",dayf = "", dayg = "";
        if (arguments != null) {
            dayList = arguments.getStringArrayList("his_day");
            durationList = arguments.getStringArrayList("his_duration");
            calorieList = arguments.getStringArrayList("his_calorie");
            int max = dayList.size();

            if(max >= 7) {
                a = durationList.get(0);
                b = durationList.get(1);
                c = durationList.get(2);
                d = durationList.get(3);
                e = durationList.get(4);
                f = durationList.get(5);
                g = durationList.get(6);

                daya = dayList.get(0);
                dayb = dayList.get(1);
                dayc = dayList.get(2);
                dayd = dayList.get(3);
                daye = dayList.get(4);
                dayf = dayList.get(5);
                dayg = dayList.get(6);
            }
            else if(max == 6){
                a = durationList.get(0);
                b = durationList.get(1);
                c = durationList.get(2);
                d = durationList.get(3);
                e = durationList.get(4);
                f = durationList.get(5);
                g = "0";

                daya = dayList.get(0);
                dayb = dayList.get(1);
                dayc = dayList.get(2);
                dayd = dayList.get(3);
                daye = dayList.get(4);
                dayf = dayList.get(5);
                dayg = "0";
            }
            else if(max == 5){
                a = durationList.get(0);
                b = durationList.get(1);
                c = durationList.get(2);
                d = durationList.get(3);
                e = durationList.get(4);
                f = "0";
                g = "0";
                daya = dayList.get(0);
                dayb = dayList.get(1);
                dayc = dayList.get(2);
                dayd = dayList.get(3);
                daye = dayList.get(4);
                dayf = "";
                dayg = "";
            }
            else if(max == 4){
                a = durationList.get(0);
                b = durationList.get(1);
                c = durationList.get(2);
                d = durationList.get(3);
                e = "0";
                f = "0";
                g = "0";
                daya = dayList.get(0);
                dayb = dayList.get(1);
                dayc = dayList.get(2);
                dayd = dayList.get(3);
                daye = "";
                dayf = "";
                dayg = "";
            }
            else if(max == 3){
                a = durationList.get(0);
                b = durationList.get(1);
                c = durationList.get(2);
                d = "";
                e = "";
                f = "";
                g = "";
                daya = dayList.get(0);
                dayb = dayList.get(1);
                dayc = dayList.get(2);
                dayd = "";
                daye = "";
                dayf = "";
                dayg = "";
            }
            else if(max == 2){
                a = durationList.get(0);
                b = durationList.get(1);
                c = "";
                d = "";
                e = "";
                f = "";
                g = "";
                daya = dayList.get(0);
                dayb = dayList.get(1);
                dayc = "";
                dayd = "";
                daye = "";
                dayf = "";
                dayg = "";
            }
            else if(max == 1){
                a = durationList.get(0);
                b = "";
                c = "";
                d = "";
                e = "";
                f = "";
                g = "";
                daya = dayList.get(0);
                dayb = "";
                dayc = "";
                dayd = "";
                daye = "";
                dayf = "";
                dayg = "";
            }


            chartData = "chd=t:" +g + "," + f + "," +e + "," + d + "," +c + "," + b +"," + a+"&";
            chartLabel = "chxl=0:|" + dayg + "|" + dayf + "|" + daye + "|" + dayd + "|" + dayc +"|" + dayb + "|" + daya +"&";
        }
            fitChart = new FitChart(getActivity());
            String chartUrl = "http://chart.apis.google.com/chart?";
            String chartType = "cht=bvg&";
            String chartSize = "chs=450x300&";
            //chartData = "chd=t:20,35,10,50,70,88,44&";
            String chartRange = "chxr=1,0,100&";
            String chartDatascale = "chds=0,100&";
            String chartBarsize = "chbh=40,0,20&";
            String chartColor = "chco=0000FF&";
            String chartxType = "chxt=x,y&";
            String chartxStyle = "chxs=2,000000,20&";
            //String chartLabel = "chxl=0:|Mon|Tue|Wed|Thu|Fri|Sat|Sun&";
            String chartTitle = "chtt=My+Execise+Distance(Km)&";
            String chartTitlestyle = "chxs=2,000000,12&";
            String chartGridline = "chg=0,25,5,5";
            String completeUrl = chartUrl + chartType + chartSize + chartData + chartRange + chartDatascale + chartBarsize +
                    chartColor + chartxType + chartxStyle + chartLabel + chartTitle + chartTitlestyle + chartGridline;
            //fitChart.execute("http://chart.apis.google.com/chart?&cht=lc&chs=460x250&chd=t:15.3,20.5,59.7,4.5&chl=Android%201.5%7CAndroid%201.6%7CAndroid%202.1%7CAndroid%202.2&chco=ff0000|3355aa|8322c2|112233");
            // fitChart.execute("http://chart.apis.google.com/chart?cht=bhg&chs=450x200&chd=t:73,83,55,66&chxt=x,y&chxl=1:|C|Java|PHP|ASP&chxr=0,0,100&chds=0,100&chco=4D89F9&chbh=35,0,15&chg=8.33,0,5,5");
            fitChart.execute(completeUrl);
            return view;
        }


}
