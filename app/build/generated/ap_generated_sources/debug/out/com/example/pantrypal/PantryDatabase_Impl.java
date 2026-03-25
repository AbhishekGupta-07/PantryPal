package com.example.pantrypal;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings({"unchecked", "deprecation"})
public final class PantryDatabase_Impl extends PantryDatabase {
  private volatile PantryDao _pantryDao;

  private volatile SavedRecipeDao _savedRecipeDao;

  private volatile ShoppingDao _shoppingDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(4) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `pantry_items` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT, `quantity` TEXT, `expiryDate` TEXT, `price` REAL NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `saved_recipes` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `title` TEXT, `content` TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `shopping_items` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `itemName` TEXT, `isPurchased` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'ee994583b32a59db20bc2b240a05e827')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `pantry_items`");
        db.execSQL("DROP TABLE IF EXISTS `saved_recipes`");
        db.execSQL("DROP TABLE IF EXISTS `shopping_items`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsPantryItems = new HashMap<String, TableInfo.Column>(5);
        _columnsPantryItems.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPantryItems.put("name", new TableInfo.Column("name", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPantryItems.put("quantity", new TableInfo.Column("quantity", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPantryItems.put("expiryDate", new TableInfo.Column("expiryDate", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPantryItems.put("price", new TableInfo.Column("price", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysPantryItems = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesPantryItems = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoPantryItems = new TableInfo("pantry_items", _columnsPantryItems, _foreignKeysPantryItems, _indicesPantryItems);
        final TableInfo _existingPantryItems = TableInfo.read(db, "pantry_items");
        if (!_infoPantryItems.equals(_existingPantryItems)) {
          return new RoomOpenHelper.ValidationResult(false, "pantry_items(com.example.pantrypal.PantryItem).\n"
                  + " Expected:\n" + _infoPantryItems + "\n"
                  + " Found:\n" + _existingPantryItems);
        }
        final HashMap<String, TableInfo.Column> _columnsSavedRecipes = new HashMap<String, TableInfo.Column>(3);
        _columnsSavedRecipes.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSavedRecipes.put("title", new TableInfo.Column("title", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSavedRecipes.put("content", new TableInfo.Column("content", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysSavedRecipes = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesSavedRecipes = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoSavedRecipes = new TableInfo("saved_recipes", _columnsSavedRecipes, _foreignKeysSavedRecipes, _indicesSavedRecipes);
        final TableInfo _existingSavedRecipes = TableInfo.read(db, "saved_recipes");
        if (!_infoSavedRecipes.equals(_existingSavedRecipes)) {
          return new RoomOpenHelper.ValidationResult(false, "saved_recipes(com.example.pantrypal.SavedRecipe).\n"
                  + " Expected:\n" + _infoSavedRecipes + "\n"
                  + " Found:\n" + _existingSavedRecipes);
        }
        final HashMap<String, TableInfo.Column> _columnsShoppingItems = new HashMap<String, TableInfo.Column>(3);
        _columnsShoppingItems.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsShoppingItems.put("itemName", new TableInfo.Column("itemName", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsShoppingItems.put("isPurchased", new TableInfo.Column("isPurchased", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysShoppingItems = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesShoppingItems = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoShoppingItems = new TableInfo("shopping_items", _columnsShoppingItems, _foreignKeysShoppingItems, _indicesShoppingItems);
        final TableInfo _existingShoppingItems = TableInfo.read(db, "shopping_items");
        if (!_infoShoppingItems.equals(_existingShoppingItems)) {
          return new RoomOpenHelper.ValidationResult(false, "shopping_items(com.example.pantrypal.ShoppingItem).\n"
                  + " Expected:\n" + _infoShoppingItems + "\n"
                  + " Found:\n" + _existingShoppingItems);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "ee994583b32a59db20bc2b240a05e827", "aab7e401dc5725663bb58e9e908b0e22");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "pantry_items","saved_recipes","shopping_items");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `pantry_items`");
      _db.execSQL("DELETE FROM `saved_recipes`");
      _db.execSQL("DELETE FROM `shopping_items`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(PantryDao.class, PantryDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(SavedRecipeDao.class, SavedRecipeDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ShoppingDao.class, ShoppingDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public PantryDao pantryDao() {
    if (_pantryDao != null) {
      return _pantryDao;
    } else {
      synchronized(this) {
        if(_pantryDao == null) {
          _pantryDao = new PantryDao_Impl(this);
        }
        return _pantryDao;
      }
    }
  }

  @Override
  public SavedRecipeDao savedRecipeDao() {
    if (_savedRecipeDao != null) {
      return _savedRecipeDao;
    } else {
      synchronized(this) {
        if(_savedRecipeDao == null) {
          _savedRecipeDao = new SavedRecipeDao_Impl(this);
        }
        return _savedRecipeDao;
      }
    }
  }

  @Override
  public ShoppingDao shoppingDao() {
    if (_shoppingDao != null) {
      return _shoppingDao;
    } else {
      synchronized(this) {
        if(_shoppingDao == null) {
          _shoppingDao = new ShoppingDao_Impl(this);
        }
        return _shoppingDao;
      }
    }
  }
}
