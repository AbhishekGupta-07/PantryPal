package com.example.pantrypal;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings({"unchecked", "deprecation"})
public final class PantryDao_Impl implements PantryDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<PantryItem> __insertionAdapterOfPantryItem;

  private final EntityDeletionOrUpdateAdapter<PantryItem> __deletionAdapterOfPantryItem;

  private final EntityDeletionOrUpdateAdapter<PantryItem> __updateAdapterOfPantryItem;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public PantryDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfPantryItem = new EntityInsertionAdapter<PantryItem>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `pantry_items` (`id`,`name`,`quantity`,`expiryDate`,`price`) VALUES (nullif(?, 0),?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final PantryItem entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getName());
        }
        if (entity.getQuantity() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getQuantity());
        }
        if (entity.getExpiryDate() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getExpiryDate());
        }
        statement.bindDouble(5, entity.getPrice());
      }
    };
    this.__deletionAdapterOfPantryItem = new EntityDeletionOrUpdateAdapter<PantryItem>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `pantry_items` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final PantryItem entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfPantryItem = new EntityDeletionOrUpdateAdapter<PantryItem>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `pantry_items` SET `id` = ?,`name` = ?,`quantity` = ?,`expiryDate` = ?,`price` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final PantryItem entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getName());
        }
        if (entity.getQuantity() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getQuantity());
        }
        if (entity.getExpiryDate() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getExpiryDate());
        }
        statement.bindDouble(5, entity.getPrice());
        statement.bindLong(6, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM pantry_items";
        return _query;
      }
    };
  }

  @Override
  public void insertItem(final PantryItem item) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfPantryItem.insert(item);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteItem(final PantryItem item) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfPantryItem.handle(item);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void updateItem(final PantryItem item) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfPantryItem.handle(item);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteAll() {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAll.acquire();
    try {
      __db.beginTransaction();
      try {
        _stmt.executeUpdateDelete();
        __db.setTransactionSuccessful();
      } finally {
        __db.endTransaction();
      }
    } finally {
      __preparedStmtOfDeleteAll.release(_stmt);
    }
  }

  @Override
  public List<PantryItem> getAllItems() {
    final String _sql = "SELECT * FROM pantry_items ORDER BY id DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
      final int _cursorIndexOfQuantity = CursorUtil.getColumnIndexOrThrow(_cursor, "quantity");
      final int _cursorIndexOfExpiryDate = CursorUtil.getColumnIndexOrThrow(_cursor, "expiryDate");
      final int _cursorIndexOfPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "price");
      final List<PantryItem> _result = new ArrayList<PantryItem>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final PantryItem _item;
        _item = new PantryItem();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _item.setId(_tmpId);
        final String _tmpName;
        if (_cursor.isNull(_cursorIndexOfName)) {
          _tmpName = null;
        } else {
          _tmpName = _cursor.getString(_cursorIndexOfName);
        }
        _item.setName(_tmpName);
        final String _tmpQuantity;
        if (_cursor.isNull(_cursorIndexOfQuantity)) {
          _tmpQuantity = null;
        } else {
          _tmpQuantity = _cursor.getString(_cursorIndexOfQuantity);
        }
        _item.setQuantity(_tmpQuantity);
        final String _tmpExpiryDate;
        if (_cursor.isNull(_cursorIndexOfExpiryDate)) {
          _tmpExpiryDate = null;
        } else {
          _tmpExpiryDate = _cursor.getString(_cursorIndexOfExpiryDate);
        }
        _item.setExpiryDate(_tmpExpiryDate);
        final double _tmpPrice;
        _tmpPrice = _cursor.getDouble(_cursorIndexOfPrice);
        _item.setPrice(_tmpPrice);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public int countTotal() {
    final String _sql = "SELECT COUNT(*) FROM pantry_items";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _result;
      if (_cursor.moveToFirst()) {
        _result = _cursor.getInt(0);
      } else {
        _result = 0;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public int countHasExpiry() {
    final String _sql = "SELECT COUNT(*) FROM pantry_items WHERE expiryDate IS NOT NULL AND expiryDate != ''";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _result;
      if (_cursor.moveToFirst()) {
        _result = _cursor.getInt(0);
      } else {
        _result = 0;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
