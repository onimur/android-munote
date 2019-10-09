package com.onimus.munote.business;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.io.File;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

public class ManageDirectoryTest {
    private static final String ENVIRONMENT = "anyPublicDirectory";

    @Mock
    private Context context;

    private ManageDirectory mdSpy;
    private ManageDirectory md;

    @Before
    public void setUp() {
        mdSpy = spy(new ManageDirectory(context));
        doReturn(new File("root")).when(mdSpy).takeAPIPatch(null);
        doReturn(new File("publicDirectory")).when(mdSpy).takeAPIPatch(ENVIRONMENT);
        md = new ManageDirectory(context);
    }

    @Test
    public void createPublicDirectoryFileForVariousApiIsValid() {
        File file = mdSpy.createPublicDirectoryFileForVariousApi("folderTest");
        assertTrue(file.exists());
        assertTrue(file.isDirectory());
        assertThat(file.getPath(), is("root\\folderTest"));

        File file2 = mdSpy.createPublicDirectoryFileForVariousApi("folderTest", ENVIRONMENT);
        assertTrue(file2.exists());
        assertTrue(file2.isDirectory());
        assertThat(file2.getPath(), is("publicDirectory\\folderTest"));
    }

    @Test
    public void createFileIsValidFile() {
        File file = md.createFolder(new File("mainFolder"), "file.jpg");
        assertTrue(file.exists());
        assertTrue(file.isDirectory());
        assertThat(file.getPath(), is("mainFolder\\file.jpg"));
    }

    @Test
    public void createFolderIsValidFolder() {
        File file = md.createFolder(new File("mainFolder"), "folder");
        assertTrue(file.exists());
        assertTrue(file.isDirectory());
        assertThat(file.getPath(), is("mainFolder\\folder"));

    }
}