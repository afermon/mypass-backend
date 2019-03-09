import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ISecret } from 'app/shared/model/secret.model';

type EntityResponseType = HttpResponse<ISecret>;
type EntityArrayResponseType = HttpResponse<ISecret[]>;

@Injectable({ providedIn: 'root' })
export class SecretService {
    public resourceUrl = SERVER_API_URL + 'api/secrets';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/secrets';

    constructor(protected http: HttpClient) {}

    create(secret: ISecret): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(secret);
        return this.http
            .post<ISecret>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(secret: ISecret): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(secret);
        return this.http
            .put<ISecret>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<ISecret>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<ISecret[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<ISecret[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    protected convertDateFromClient(secret: ISecret): ISecret {
        const copy: ISecret = Object.assign({}, secret, {
            modified: secret.modified != null && secret.modified.isValid() ? secret.modified.toJSON() : null
        });
        return copy;
    }

    protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
        if (res.body) {
            res.body.modified = res.body.modified != null ? moment(res.body.modified) : null;
        }
        return res;
    }

    protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        if (res.body) {
            res.body.forEach((secret: ISecret) => {
                secret.modified = secret.modified != null ? moment(secret.modified) : null;
            });
        }
        return res;
    }
}
