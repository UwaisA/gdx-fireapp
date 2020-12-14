/*
 * Copyright 2019 mk
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package pl.mk5.gdx.fireapp.ios.database;

import com.badlogic.gdx.utils.Array;

import org.robovm.pods.firebase.database.FIRDataEventType;
import org.robovm.pods.firebase.database.FIRDatabaseQuery;

import pl.mk5.gdx.fireapp.database.ChildEventType;
import pl.mk5.gdx.fireapp.database.validators.ArgumentsValidator;
import pl.mk5.gdx.fireapp.database.validators.OnChildValidator;
import pl.mk5.gdx.fireapp.promises.ConverterPromise;
import pl.mk5.gdx.fireapp.promises.FutureListenerPromise;

class QueryOnChildChange<R> extends DatabaseQuery<R> {

    QueryOnChildChange(Database databaseDistribution, String databasePath) {
        super(databaseDistribution, databasePath);
    }

    @Override
    protected ArgumentsValidator createArgumentsValidator() {
        return new OnChildValidator();
    }

    @Override
    @SuppressWarnings("unchecked")
    protected R run() {
        Array<ChildEventType> childEventTypes = new Array<>((ChildEventType[]) arguments.get(1));
        for (ChildEventType type : childEventTypes) {
            FIRDataEventType databaseType = getDatabaseEventType(type);
            long handle = filtersProvider.applyFiltering().observeEvent(databaseType,
                    new SnapshotProcessorBlock((ConverterPromise) promise, orderByClause, (Class) arguments.get(0)),
                    new SnapshotCancelBlock((ConverterPromise) promise));
            ((FutureListenerPromise) promise).onCancel(new CancelAction(handle, query));
        }
        return null;
    }

    private FIRDataEventType getDatabaseEventType(ChildEventType childEventType) {
        switch (childEventType) {
            case ADDED:
                return FIRDataEventType.ChildAdded;
            case CHANGED:
                return FIRDataEventType.ChildChanged;
            case MOVED:
                return FIRDataEventType.ChildMoved;
            case REMOVED:
                return FIRDataEventType.ChildRemoved;
            default:
                throw new IllegalStateException();
        }
    }
}
