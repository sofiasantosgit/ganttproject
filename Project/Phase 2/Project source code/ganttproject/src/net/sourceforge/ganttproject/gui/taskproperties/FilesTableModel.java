/*
Copyright 2003-2012 Dmitry Barashev, GanttProject Team

This file is part of GanttProject, an opensource project management tool.

GanttProject is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

GanttProject is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with GanttProject.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.ganttproject.gui.taskproperties;

import net.sourceforge.ganttproject.language.GanttLanguage;
import net.sourceforge.ganttproject.task.File;
import net.sourceforge.ganttproject.task.FileCollection;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Pedro Grilo
 * @author Guilherme Fernandes
 */
public class FilesTableModel extends AbstractTableModel {
    private static final boolean EDITABLE = true;
    private static final boolean NOT_EDITABLE = false;

    private final FileCollection myFiles;

    public FilesTableModel(FileCollection fileColection) {
        myFiles = fileColection;
    }

    public void delete(int[] selectedRows) {
        List<File> selected = new ArrayList<File>();
        for (int row : selectedRows) {
            if (row < myFiles.size()) {
                selected.add(myFiles.get(row));
            }
        }
        myFiles.removeAll(selected);
        fireTableDataChanged();
    }

    @Override
    public int getColumnCount() {
        return MyColumn.values().length;
    }

    @Override
    public int getRowCount() {
        return myFiles.size() + 1;
    }

    @Override
    public String getColumnName(int col) {
        return MyColumn.values()[col].getCaption();
    }

    @Override
    public Object getValueAt(int row, int col) {
        assert row >= 0 && row < getRowCount() && col >= 0 && col < getColumnCount();
        if (row == myFiles.size()) {
            return "";
        }
        File file = myFiles.get(row);
        MyColumn column = MyColumn.values()[col];
        switch (column) {
            case FILE_NAME: {
                return file.getFileName();
            }
            case FILE_URL: {
                return file.getURL();
            }
            default:
                throw new IllegalArgumentException("Illegal row number=" + row);
        }
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        MyColumn column = MyColumn.values()[col];
        return row == getRowCount() ? column == MyColumn.FILE_URL : column.isEditable();
    }

    public void addFile(File file) {
        myFiles.addFile(file);
        fireTableDataChanged();
    }

    public File getFile(int row) {
        return myFiles.get(row);
    }

    public enum MyColumn {
        FILE_NAME(GanttLanguage.getInstance().getText("file"), FilesTableModel.NOT_EDITABLE),
        FILE_URL(GanttLanguage.getInstance().getText("url"), FilesTableModel.NOT_EDITABLE);

        private final String myCaption;
        private final boolean isEditable;

        MyColumn(String caption, boolean isEditable) {
            myCaption = caption;
            this.isEditable = isEditable;
        }

        public String getCaption() {
            return myCaption;
        }

        public boolean isEditable() {
            return isEditable;
        }

        TableColumn getTableColumn(JTable table) {
            return table.getColumnModel().getColumn(this.ordinal());
        }
    }
}