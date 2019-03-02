/*
 * Created on Nov 23, 2004
 *
 */
package cn.org.rapid.framework.generator.util;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * @author MF1180
 * Hashtable that maintains the input order of the data elements - Note
 * only put, putall, clear and remove maintains the ordered list
 */
public class ListHashTable extends Hashtable {
    protected List orderedKeys = new ArrayList();

    @Override
    public synchronized void clear() {
        super.clear();
        orderedKeys = new ArrayList();
    }

    @Override
    public synchronized Object put(final Object aKey, final Object aValue) {
        if (orderedKeys.contains(aKey)) {
            final int pos = orderedKeys.indexOf(aKey);
            orderedKeys.remove(pos);
            orderedKeys.add(pos, aKey);
        } else {
            if (aKey instanceof Integer) {
                final Integer key = (Integer) aKey;
                final int pos = getFirstKeyGreater(key.intValue());
                if (pos >= 0) {
                    orderedKeys.add(pos, aKey);
                } else {
                    orderedKeys.add(aKey);
                }
            } else {
                orderedKeys.add(aKey);
            }
        }
        return super.put(aKey, aValue);
    }

    /**
     * @param aKey
     * @returns calculate position at which the first key is greater
     * otherwise return -1 if no key can be found which is greater
     */
    private int getFirstKeyGreater(final int aKey) {
        int pos = 0;
        final int numKeys = getOrderedKeys().size();
        for (int i = 0; i < numKeys; i++) {
            final Integer key = (Integer) getOrderedKey(i);
            final int keyval = key.intValue();
            if (keyval < aKey) {
                ++pos;
            } else {
                break;
            }
        }
        if (pos >= numKeys) {
            pos = -1;
        }
        return pos;
    }

    @Override
    public synchronized Object remove(final Object aKey) {
        if (orderedKeys.contains(aKey)) {
            final int pos = orderedKeys.indexOf(aKey);
            orderedKeys.remove(pos);
        }
        return super.remove(aKey);
    }

    /**
     * This method reorders the ListHashtable only if the keys
     * used are integer keys.
     */
    public void reorderIntegerKeys() {
        final List keys = getOrderedKeys();
        final int numKeys = keys.size();
        if (numKeys <= 0) {
            return;
        }

        if (!(getOrderedKey(0) instanceof Integer)) {
            return;
        }

        final List newKeys = new ArrayList();
        final List newValues = new ArrayList();

        for (int i = 0; i < numKeys; i++) {
            final Integer key = (Integer) getOrderedKey(i);
            final Object val = getOrderedValue(i);
            final int numNew = newKeys.size();
            int pos = 0;
            for (int j = 0; j < numNew; j++) {
                final Integer newKey = (Integer) newKeys.get(j);
                if (newKey.intValue() < key.intValue()) {
                    ++pos;
                } else {
                    break;
                }
            }
            if (pos >= numKeys) {
                newKeys.add(key);
                newValues.add(val);
            } else {
                newKeys.add(pos, key);
                newValues.add(pos, val);
            }
        }
        // reset the contents
        this.clear();
        for (int l = 0; l < numKeys; l++) {
            put(newKeys.get(l), newValues.get(l));
        }
    }

    @Override
    public String toString() {
        final StringBuffer x = new StringBuffer();
        x.append("Ordered Keys: ");
        final int numKeys = orderedKeys.size();
        x.append("[");
        for (int i = 0; i < numKeys; i++) {
            x.append(orderedKeys.get(i) + " ");
        }
        x.append("]\n");

        x.append("Ordered Values: ");
        x.append("[");

        for (int j = 0; j < numKeys; j++) {
            x.append(getOrderedValue(j) + " ");
        }
        x.append("]\n");
        return x.toString();
    }

    public void merge(final ListHashTable newTable) {
        // This merges the newtable with the current one
        final int num = newTable.size();
        for (int i = 0; i < num; i++) {
            final Object aKey = newTable.getOrderedKey(i);
            final Object aVal = newTable.getOrderedValue(i);
            this.put(aKey, aVal);
        }
    }

    /**
     * @return Returns the orderedKeys.
     */
    public List getOrderedKeys() {
        return orderedKeys;
    }

    public Object getOrderedKey(final int i) {
        return getOrderedKeys().get(i);
    }

    /**
     * This method looks through the list of values and returns the key
     * associated with the value.. Otherwise if not found, null is returned
     *
     * @param aValue
     * @return
     */
    public Object getKeyForValue(final Object aValue) {
        final int num = getOrderedValues().size();
        for (int i = 0; i < num; i++) {
            final Object tmpVal = getOrderedValue(i);
            if (tmpVal.equals(aValue)) {
                return getOrderedKey(i);
            }
        }
        return null;
    }

    public List getOrderedValues() {
        final List values = new ArrayList();
        final int numKeys = orderedKeys.size();
        for (int i = 0; i < numKeys; i++) {
            values.add(get(getOrderedKey(i)));
        }
        return values;
    }

    public Object getOrderedValue(final int i) {
        return get(getOrderedKey(i));
    }

    @Override
    public synchronized boolean equals(final Object o) {
        return super.equals(o);
    }

    @Override
    public synchronized int hashCode() {
        return super.hashCode();
    }
}
