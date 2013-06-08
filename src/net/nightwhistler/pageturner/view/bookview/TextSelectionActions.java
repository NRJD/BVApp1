/*
 * Copyright (C) 2013 Alex Kuiper
 *
 * This file is part of PageTurner
 *
 * PageTurner is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PageTurner is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with PageTurner.  If not, see <http://www.gnu.org/licenses/>.*
 */

package net.nightwhistler.pageturner.view.bookview;

import android.graphics.Color;
import android.annotation.TargetApi;
import android.os.Build;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import net.nightwhistler.pageturner.R;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class TextSelectionActions implements ActionMode.Callback {

	private TextSelectionCallback callBack;
	private BookView bookView;

	public TextSelectionActions(TextSelectionCallback callBack,
			BookView bookView) {
		this.callBack = callBack;
		this.bookView = bookView;
	}

	@Override
	public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

        mode.finish();

		return true;
	}

	@Override
	public boolean onCreateActionMode(final ActionMode mode, Menu menu) {

		menu.removeItem(android.R.id.selectAll);

		if (callBack.isDictionaryAvailable()) {
			menu.add(R.string.dictionary_lookup)
				.setOnMenuItemClickListener(new OnMenuItemClickListener() {
				@Override
				public boolean onMenuItemClick(android.view.MenuItem item) {
					callBack.lookupDictionary(bookView.getSelectedText());
                    mode.finish();
					return true;
				}
			});
		}

		menu.add(R.string.wikipedia_lookup)
			.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(android.view.MenuItem item) {
				callBack.lookupWikipedia(bookView.getSelectedText());
                mode.finish();
				return true;
			}
		});

		menu.add(R.string.google_lookup)
			.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(android.view.MenuItem item) {
				callBack.lookupGoogle(bookView.getSelectedText());
                mode.finish();
				return true;
			}
		});

        menu.add("Highlight")
            .setOnMenuItemClickListener(new OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(android.view.MenuItem item) {
                callBack.highLight(bookView.getSelectionStart(), bookView.getSelectionEnd());
                mode.finish();
                return true;
            }
        });

        return true;
    }

	@Override
	public void onDestroyActionMode(ActionMode mode) {

	}

	@Override
	public boolean onPrepareActionMode(ActionMode mode, Menu menu) {

		return true;
	}

}
