package com.campus02.todocheckliste.activities;

import android.content.DialogInterface;

import java.net.MalformedURLException;

public interface DialogCloseListener {
    public void handleDialogClose(DialogInterface dialog) throws MalformedURLException;
}