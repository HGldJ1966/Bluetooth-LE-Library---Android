package uk.co.alt236.bluetoothlelib.device;

import java.util.Arrays;

import uk.co.alt236.bluetoothlelib.util.ByteUtils;
import android.bluetooth.BluetoothDevice;
import android.os.Parcel;

public class GimbalDevice extends BluetoothLeDevice {

    private static final byte[] GIMBAL_IDENTIFIER_PART1 =
            ByteUtils.hexStringToByteArray("1107AD7700C6A00099B2E2114C24");
    private static final byte[] GIMBAL_IDENTIFIER_PART2 =
            ByteUtils.hexStringToByteArray("0C960CFF8C00");

    protected String mId;
    protected byte[] mFlags;

    public static BluetoothLeDevice createFromBytes(BluetoothDevice _device, int _rssi,
            byte[] _scanRecord, long _timestamp) {
        try {
            byte[] identifier1 = Arrays.copyOfRange(_scanRecord, 0, 14);
            byte[] identifier2 = Arrays.copyOfRange(_scanRecord, 16, 22);
            if (Arrays.equals(identifier1, GIMBAL_IDENTIFIER_PART1)
                    && Arrays.equals(identifier2, GIMBAL_IDENTIFIER_PART2)) {
                byte[] flags = Arrays.copyOfRange(_scanRecord, 14, 16);
                byte[] unique = Arrays.copyOfRange(_scanRecord, 22, 31);
                return new GimbalDevice(ByteUtils.byteArrayToHexString(unique, false), flags,
                        _device, _rssi, _scanRecord, _timestamp);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            // Could not parse scan record; it's not a Gimbal
        }
        return new BluetoothLeDevice(_device, _rssi, _scanRecord, _timestamp);
    }

    public GimbalDevice(String _gimbalId, byte[] _flags, BluetoothDevice _device, int _rssi,
            byte[] _scanRecord, long _timestamp) {
        super(_device, _rssi, _scanRecord, _timestamp);
        mId = _gimbalId;
        mFlags = _flags;
    }

    protected GimbalDevice(Parcel _in) {
        super(_in);
    }

    @Override
    public String getId() {
        return mId;
    }

    @Override
    public String getName() {
        return mId;
    }

    public byte[] getFlags() {
        return mFlags;
    }

}
