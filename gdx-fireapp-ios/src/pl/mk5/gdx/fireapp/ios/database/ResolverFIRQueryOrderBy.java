/*
 * Copyright 2018 mk
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

import org.robovm.pods.firebase.database.FIRDatabaseQuery;

import pl.mk5.gdx.fireapp.database.OrderByClause;
import pl.mk5.gdx.fireapp.database.OrderByResolver;

class ResolverFIRQueryOrderBy implements OrderByResolver<FIRDatabaseQuery, FIRDatabaseQuery> {
    @Override
    public FIRDatabaseQuery resolve(OrderByClause orderByClause, FIRDatabaseQuery target) {
        switch (orderByClause.getOrderByMode()) {
            case ORDER_BY_CHILD:
                return target.queryOrderedByChild(orderByClause.getArgument());
            case ORDER_BY_KEY:
                return target.queryOrderedByKey();
            case ORDER_BY_VALUE:
                return target.queryOrderedByValue();
            default:
                throw new IllegalStateException();
        }
    }
}
