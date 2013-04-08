package com.spanisharmada.droidcon.ui.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.dsi.ant.plugins.AntPluginMsgDefines;
import com.dsi.ant.plugins.AntPluginPcc.IDeviceStateChangeReceiver;
import com.dsi.ant.plugins.AntPluginPcc.IPluginAccessResultReceiver;
import com.dsi.ant.plugins.antplus.pcc.AntPlusGeocachePcc;
import com.dsi.ant.plugins.antplus.pcc.AntPlusGeocachePcc.GeocacheDeviceData;
import com.dsi.ant.plugins.antplus.pcc.AntPlusGeocachePcc.IAvailableDeviceListReceiver;
import com.dsi.ant.plugins.antplus.pcc.AntPlusGeocachePcc.IDataDownloadFinishedReceiver;
import com.dsi.ant.plugins.antplus.pcc.AntPlusGeocachePcc.RequestStatusCode;
import com.spanisharmada.droidcon.R;
import com.spanisharmada.droidcon.data.model.SAProfileData;
import com.spanisharmada.droidcon.ui.activities.SAProfileDetailsActivity;

public class SAScanFragment extends Fragment {

    private final String LOG_CLASS = getClass().getSimpleName();
    private final String ACCEPTED_RPOFILE_NAME = "SALOVE";

    private int mDeviceID;

    private ImageView mHeartImage;
    private Button mScanBtn;

    private AntPlusGeocachePcc mGeoPcc;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(final LayoutInflater inflater,
            final ViewGroup container, final Bundle savedInstanceState) {

        RelativeLayout scanLayout = (RelativeLayout) inflater.inflate(
                R.layout.scan_list_fragment, container, false);

        mHeartImage = (ImageView) scanLayout.findViewById(R.id.SA_heartImage);
        mScanBtn = (Button) scanLayout.findViewById(R.id.SA_scanBtn);
        mScanBtn.setOnClickListener(new android.view.View.OnClickListener() {

            @Override
            public void onClick(final View v) {
                startScanProfiles();

            }
        });

        return scanLayout;
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mHeartImage.setVisibility(View.GONE);
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
     * This is the final step, after we click on the heart image and go to the
     * {@link SAProfileDetailsActivity}
     */
    public void heartPressed() {

        mGeoPcc.requestDeviceData(mDeviceID, true,
        // Display the results if successful or report failures to user
                new IDataDownloadFinishedReceiver() {
                    @Override
                    public void onNewDataDownloadFinished(final int statusCode,
                            final GeocacheDeviceData downloadedData) {
                        StringBuilder error = new StringBuilder(
                                "Error Downloading Data: ");
                        switch (statusCode) {
                            case RequestStatusCode.SUCCESS:
                                Intent intent = new Intent(getActivity(),
                                        SAProfileDetailsActivity.class);
                                intent.putExtra(
                                        SAProfileData.PROFILE_DATA_KEY,
                                        downloadedData.programmableData.hintString);
                                startActivity(intent);
                                return;
                            case RequestStatusCode.FAIL_DEVICE_NOT_IN_LIST:
                                error.append("Device no longer in list");
                                break;
                            case RequestStatusCode.FAIL_ALREADY_BUSY_EXTERNAL:
                                error.append("Device is busy");
                                break;
                            case RequestStatusCode.FAIL_DEVICE_COMMUNICATION_FAILURE:
                                error.append("Communication with device failed");
                                break;
                        }
                    }
                }, null);
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
                        // mDeviceListDisplay.clear();

                        if (deviceIDs.length != 0) {
                            for (int i = 0; i < deviceIDs.length; ++i) {

                                Log.d(LOG_CLASS, "Item: " + deviceIDs[i]);

                                if (deviceIdentifierStrings[i]
                                        .contains(ACCEPTED_RPOFILE_NAME)) {
                                    mDeviceID = deviceIDs[i];

                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {

                                            mHeartImage
                                                    .setVisibility(View.VISIBLE);
                                        }
                                    });
                                }
                            }
                        }
                    }
                });
    }
}
