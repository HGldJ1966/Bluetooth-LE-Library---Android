package uk.co.alt236.btlescan.util;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

public final class BluetoothUtils {

    private final BluetoothAdapter mBluetoothAdapter;
    private final BluetoothManager mBluetoothManager;

    public final static int REQUEST_ENABLE_BT = 2001;

    public BluetoothUtils(Activity _activity) {
        mBluetoothManager = (BluetoothManager) _activity
                .getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = mBluetoothManager.getAdapter();
    }

    public void askUserToEnableBluetoothIfNeeded(Activity _activity) {
        if (isBluetoothLeSupported(_activity)
                && (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled())) {
            final Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            _activity.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }

    public BluetoothAdapter getBluetoothAdapter() {
        return mBluetoothAdapter;
    }

    public boolean isBluetoothLeSupported(Context _context) {
        return _context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE);
    }

    public boolean isBluetoothOn() {
        if (mBluetoothAdapter == null) {
            return false;
        } else {
            return mBluetoothAdapter.isEnabled();
        }
    }

}
