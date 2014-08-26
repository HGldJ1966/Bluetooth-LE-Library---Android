package uk.co.alt236.btlescan.util;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothAdapter.LeScanCallback;
import android.os.Handler;
import android.util.Log;

public class BluetoothLeScanner {

    private static BluetoothLeScanner sInstance;

    private final Handler mHandler;
    private BluetoothAdapter.LeScanCallback mLeScanCallback;
    private BluetoothUtils mBluetoothUtils;
    private boolean mScanning;
    private int mScanDuration;

    public static BluetoothLeScanner getInstance(LeScanCallback _leScanCallback, Activity _activity) {
        if (sInstance == null) {
            sInstance = new BluetoothLeScanner(_leScanCallback, _activity);
        } else {
            sInstance.setOwner(_leScanCallback, _activity);
        }
        return sInstance;
    }

    private BluetoothLeScanner(BluetoothAdapter.LeScanCallback leScanCallback,
            Activity _activity) {
        mHandler = new Handler();
        setOwner(leScanCallback, _activity);
    }

    private void setOwner(BluetoothAdapter.LeScanCallback leScanCallback,
            Activity _activity) {
        boolean startScanning = false;
        if (mScanning) {
            // Stop scanning
            scanLeDevice(0, false);
            startScanning = true;
        }
        mLeScanCallback = leScanCallback;
        if (mBluetoothUtils == null) {
            // No need to recreate as we don't hold a reference to the activity
            mBluetoothUtils = new BluetoothUtils(_activity);
        }
        if (startScanning) {
            // Start scanning
            scanLeDevice(mScanDuration, true);
        }
    }

    public BluetoothUtils getBluetoothUtils() {
        return mBluetoothUtils;
    }

    public boolean isScanning() {
        return mScanning;
    }

    public void scanLeDevice(final int duration, final boolean enable) {
        if (enable) {
            if (mScanning) {
                return;
            }
            Log.d("TAG", "~ Starting Scan");
            // Stops scanning after a pre-defined scan period.
            if (duration > 0) {
                mHandler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        Log.d("TAG", "~ Stopping Scan (timeout)");
                        mScanning = false;
                        mBluetoothUtils.getBluetoothAdapter().stopLeScan(mLeScanCallback);
                    }
                }, duration);
            }
            mScanDuration = duration;
            mScanning = true;
            mBluetoothUtils.getBluetoothAdapter().startLeScan(mLeScanCallback);
        } else {
            Log.d("TAG", "~ Stopping Scan");
            mScanning = false;
            mBluetoothUtils.getBluetoothAdapter().stopLeScan(mLeScanCallback);
        }
    }

}
