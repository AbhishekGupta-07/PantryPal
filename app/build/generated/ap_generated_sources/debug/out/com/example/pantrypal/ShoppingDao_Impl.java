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
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class ShoppingDao_Impl implements ShoppingDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ShoppingItem> __insertionAdapterOfShoppingItem;

  private final EntityDeletionOrUpdateAdapter<ShoppingItem> __deletionAdapterOfShoppingItem;

  private final EntityDeletionOrUpdateAdapter<ShoppingItem> __updateAdapterOfShoppingItem;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public ShoppingDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfShoppingItem = new EntityInsertionAdapter<ShoppingItem>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `shopping_items` (`id`,`itemName`,`isPurchased`) VALUES (nullif(?, 0),?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final ShoppingItem entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getItemName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getItemName());
        }
        final int _tmp = entity.isPurchased() ? 1 : 0;
        statement.bindLong(3, _tmp);
      }
    };
    this.__deletionAdapterOfShoppingItem = new EntityDeletionOrUpdateAdapter<ShoppingItem>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `shopping_items` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final ShoppingItem entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfShoppingItem = new EntityDeletionOrUpdateAdapter<ShoppingItem>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `shopping_items` SET `id` = ?,`itemName` = ?,`isPurchased` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final ShoppingItem entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getItemName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getItemName());
        }
        final int _tmp = entity.isPurchased() ? 1 : 0;
        statement.bindLong(3, _tmp);
        statement.bindLong(4, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM shopping_items";
        return _query;
      }
    };
  }

  @Override
  public void insert(final ShoppingItem item) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfShoppingItem.insert(item);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(final ShoppingItem item) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfShoppingItem.handle(item);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void update(final ShoppingItem item) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfShoppingItem.handle(item);
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
  public List<ShoppingItem> getAllItems() {
    final String _sql = "SELECT * FROM shopping_items ORDER BY id DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfItemName = CursorUtil.getColumnIndexOrThrow(_cursor, "itemName");
      final int _cursorIndexOfIsPurchased = CursorUtil.getColumnIndexOrThrow(_cursor, "isPurchased");
      final List<ShoppingItem> _result = new ArrayList<ShoppingItem>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final ShoppingItem _item;
        _item = new ShoppingItem();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _item.setId(_tmpId);
        final String _tmpItemName;
        if (_cursor.isNull(_cursorIndexOfItemName)) {
          _tmpItemName = null;
        } else {
          _tmpItemName = _cursor.getString(_cursorIndexOfItemName);
        }
        _item.setItemName(_tmpItemName);
        final boolean _tmpIsPurchased;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfIsPurchased);
        _tmpIsPurchased = _tmp != 0;
        _item.setPurchased(_tmpIsPurchased);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<ShoppingItem> getAllItemsSorted() {
    final String _sql = "SELECT * FROM shopping_items ORDER BY isPurchased ASC, id DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfItemName = CursorUtil.getColumnIndexOrThrow(_cursor, "itemName");
      final int _cursorIndexOfIsPurchased = CursorUtil.getColumnIndexOrThrow(_cursor, "isPurchased");
      final List<ShoppingItem> _result = new ArrayList<ShoppingItem>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final ShoppingItem _item;
        _item = new ShoppingItem();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _item.setId(_tmpId);
        final String _tmpItemName;
        if (_cursor.isNull(_cursorIndexOfItemName)) {
          _tmpItemName = null;
        } else {
          _tmpItemName = _cursor.getString(_cursorIndexOfItemName);
        }
        _item.setItemName(_tmpItemName);
        final boolean _tmpIsPurchased;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfIsPurchased);
        _tmpIsPurchased = _tmp != 0;
        _item.setPurchased(_tmpIsPurchased);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<ShoppingItem> getPendingItems() {
    final String _sql = "SELECT * FROM shopping_items WHERE isPurchased = 0 ORDER BY id DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfItemName = CursorUtil.getColumnIndexOrThrow(_cursor, "itemName");
      final int _cursorIndexOfIsPurchased = CursorUtil.getColumnIndexOrThrow(_cursor, "isPurchased");
      final List<ShoppingItem> _result = new ArrayList<ShoppingItem>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final ShoppingItem _item;
        _item = new ShoppingItem();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _item.setId(_tmpId);
        final String _tmpItemName;
        if (_cursor.isNull(_cursorIndexOfItemName)) {
          _tmpItemName = null;
        } else {
          _tmpItemName = _cursor.getString(_cursorIndexOfItemName);
        }
        _item.setItemName(_tmpItemName);
        final boolean _tmpIsPurchased;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfIsPurchased);
        _tmpIsPurchased = _tmp != 0;
        _item.setPurchased(_tmpIsPurchased);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<ShoppingItem> getCompletedItems() {
    final String _sql = "SELECT * FROM shopping_items WHERE isPurchased = 1 ORDER BY id DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfItemName = CursorUtil.getColumnIndexOrThrow(_cursor, "itemName");
      final int _cursorIndexOfIsPurchased = CursorUtil.getColumnIndexOrThrow(_cursor, "isPurchased");
      final List<ShoppingItem> _result = new ArrayList<ShoppingItem>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final ShoppingItem _item;
        _item = new ShoppingItem();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _item.setId(_tmpId);
        final String _tmpItemName;
        if (_cursor.isNull(_cursorIndexOfItemName)) {
          _tmpItemName = null;
        } else {
          _tmpItemName = _cursor.getString(_cursorIndexOfItemName);
        }
        _item.setItemName(_tmpItemName);
        final boolean _tmpIsPurchased;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfIsPurchased);
        _tmpIsPurchased = _tmp != 0;
        _item.setPurchased(_tmpIsPurchased);
        _result.add(_item);
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
