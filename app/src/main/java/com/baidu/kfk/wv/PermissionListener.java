package com.baidu.kfk.wv;

import android.support.annotation.NonNull;

/**
 * Created by colingo on 6/11/2017.
 */

public interface PermissionListener {
    /*
     * 通过授权
     * @param permission
     */
    void permissionGranted(@NonNull String[] permission);

    /**
     * 拒绝授权
     * @param permission
     */
    void permissionDenied(@NonNull String[] permission);
}
