package com.spanisharmada.droidcon.ui.fragments;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.dsi.ant.plugins.AntPluginMsgDefines;
import com.dsi.ant.plugins.AntPluginPcc.IDeviceStateChangeReceiver;
import com.dsi.ant.plugins.AntPluginPcc.IPluginAccessResultReceiver;
import com.dsi.ant.plugins.antplus.pcc.AntPlusGeocachePcc;
import com.dsi.ant.plugins.antplus.pcc.AntPlusGeocachePcc.IAvailableDeviceListReceiver;
import com.spanisharmada.droidcon.R;

public class SAScanListFragment extends Fragment {

    private ListView mProfilesListView;

    private AntPlusGeocachePcc mGeoPcc;
    List<Map<String, String>> mDeviceListDisplay;
    SimpleAdapter mAadapterDeviceListDisplay;

    private boolean mListInitComplete;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater,
            final ViewGroup container, final Bundle savedInstanceState) {

        RelativeLayout scanLayout = (RelativeLayout) inflater.inflate(
                R.layout.scan_list_fragment, container, false);

        mProfilesListView = (ListView) scanLayout
                .findViewById(R.id.SA_scanListView);

        return scanLayout;
    }

    @Override
    public void onDestroy() {
        if (mGeoPcc != null) {
            mGeoPcc.releaseAccess();
            mGeoPcc = null;
        }
        super.onDestroy();
    }

    /**
     * Initializes the stuff
     */
    private void init() {

        mAadapterDeviceListDisplay = new SimpleAdapter(getActivity(),
                mDeviceListDisplay, android.R.layout.simple_list_item_1,
                new String[] { "title" }, new int[] { android.R.id.text1 });

        mProfilesListView.setAdapter(mAadapterDeviceListDisplay);
    }

    /**
     * 
     * @param view
     */
    private void clickBtns(final View view) {

        switch (view.getId()) {

            case R.id.SA_scanBtn:

                break;
            default:
                break;
        }
    }

    /**
     * Resets the PCC connection to request access again and clears any existing
     * display data.
     */
    private void startScanProfiles() {
        // Release the old access if it exists
        if (mGeoPcc != null) {
            mGeoPcc.releaseAccess();
            mGeoPcc = null;
        }

        // Reset the device list display
        mDeviceListDisplay.clear();
        HashMap<String, String> listItem = new HashMap<String, String>();
        listItem.put("title", "No Devices Found");
        listItem.put("desc", "No results received from plugin yet...");
        mDeviceListDisplay.add(listItem);
        mAadapterDeviceListDisplay.notifyDataSetChanged();
        mListInitComplete = false;

        // Make the access request
        AntPlusGeocachePcc.requestListAndRequestAccess(getActivity(),
                new IPluginAccessResultReceiver<AntPlusGeocachePcc>() {
                    @Override
                    public void onResultReceived(
                            final AntPlusGeocachePcc result,
                            final int resultCode,
                            final int initialDeviceStateCode) {
                        switch (resultCode) {
                            case AntPluginMsgDefines.MSG_REQACC_RESULT_whatSUCCESS:
                                mGeoPcc = result;
                                // tv_status.setText(result.getDeviceName()
                                // + ": "
                                // + AntPlusGeocachePcc
                                // .statusCodeToPrintableString(initialDeviceStateCode));
                                mGeoPcc.requestCurrentDeviceList();
                                // subscribeToEvents();
                                break;
                            case AntPluginMsgDefines.MSG_REQACC_RESULT_whatCHANNELNOTAVAILABLE:
                                Toast.makeText(getActivity(),
                                        "Channel Not Available",
                                        Toast.LENGTH_SHORT).show();
                                break;
                            case AntPluginMsgDefines.MSG_REQACC_RESULT_whatOTHERFAILURE:
                                Toast.makeText(
                                        getActivity(),
                                        "RequestAccess failed. See logcat for details.",
                                        Toast.LENGTH_SHORT).show();
                                break;
                            case AntPluginMsgDefines.MSG_REQACC_RESULT_whatDEPENDENCYNOTINSTALLED:
                                AlertDialog.Builder adlgBldr = new AlertDialog.Builder(
                                        getActivity());
                                adlgBldr.setTitle("Missing Dependency");
                                adlgBldr.setMessage("The required application\n\""
                                        + AntPlusGeocachePcc
                                                .getMissingDependencyName()
                                        + "\"\n is not installed. Do you want to launch the Play Store to search for it?");
                                adlgBldr.setCancelable(true);
                                adlgBldr.setPositiveButton("Go to Store",
                                        new OnClickListener() {
                                            @Override
                                            public void onClick(
                                                    final DialogInterface dialog,
                                                    final int which) {
                                                Intent startStore = null;
                                                startStore = new Intent(
                                                        Intent.ACTION_VIEW,
                                                        Uri.parse("market://details?id="
                                                                + AntPlusGeocachePcc
                                                                        .getMissingDependencyPackageName()));
                                                startStore
                                                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                                                getActivity().startActivity(
                                                        startStore);
                                            }
                                        });
                                adlgBldr.setNegativeButton("Cancel",
                                        new OnClickListener() {
                                            @Override
                                            public void onClick(
                                                    final DialogInterface dialog,
                                                    final int which) {
                                                dialog.dismiss();
                                            }
                                        });

                                final AlertDialog waitDialog = adlgBldr
                                        .create();
                                waitDialog.show();
                                break;
                            case AntPluginMsgDefines.MSG_REQACC_RESULT_whatUSERCANCELLED:
                                break;
                            default:
                                Toast.makeText(getActivity(),
                                        "Unrecognized result: " + resultCode,
                                        Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                },
                // Receives state changes and shows it on the status display
                // line
                new IDeviceStateChangeReceiver() {
                    @Override
                    public void onDeviceStateChange(final int newDeviceState) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (newDeviceState == AntPluginMsgDefines.DeviceStateCodes.DEAD) {
                                    mGeoPcc = null;
                                }
                            }
                        });

                    }
                },
                // Receives the device list updates and displays the current
                // list
                new IAvailableDeviceListReceiver() {

                    @Override
                    public void onNewAvailableDeviceList(final int[] deviceIDs,
                            final String[] deviceIdentifierStrings,
                            final int changeCode, final int changingDeviceID) {
                        mDeviceListDisplay.clear();

                        if (deviceIDs.length != 0) {
                            for (int i = 0; i < deviceIDs.length; ++i) {
                                if (deviceIdentifierStrings[i].trim().length() == 0) {
                                    deviceIdentifierStrings[i] = "<Unprogrammed>";
                                } else if (deviceIdentifierStrings[i]
                                        .contentEquals("_________")) {
                                    deviceIdentifierStrings[i] = "<Invalid>";
                                }

                                HashMap<String, String> listItem = new HashMap<String, String>();
                                listItem.put("title",
                                        deviceIdentifierStrings[i]);
                                listItem.put("desc",
                                        Integer.toString(deviceIDs[i]));

                                mDeviceListDisplay.add(listItem);
                            }
                        } else {
                            HashMap<String, String> listItem = new HashMap<String, String>();
                            listItem.put("title", "No Devices Found");
                            listItem.put("desc",
                                    "No geocaches sensors detected in range yet...");
                            mDeviceListDisplay.add(listItem);
                        }

                        getActivity().runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                mAadapterDeviceListDisplay
                                        .notifyDataSetChanged();
                            }
                        });

                    }
                });

    }

}
