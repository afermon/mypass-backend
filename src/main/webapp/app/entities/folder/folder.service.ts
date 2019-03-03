import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IFolder } from 'app/shared/model/folder.model';

type EntityResponseType = HttpResponse<IFolder>;
type EntityArrayResponseType = HttpResponse<IFolder[]>;

@Injectable({ providedIn: 'root' })
export class FolderService {
    public resourceUrl = SERVER_API_URL + 'api/folders';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/folders';

    constructor(protected http: HttpClient) {}

    create(folder: IFolder): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(folder);
        return this.http
            .post<IFolder>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(folder: IFolder): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(folder);
        return this.http
            .put<IFolder>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IFolder>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IFolder[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IFolder[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    protected convertDateFromClient(folder: IFolder): IFolder {
        const copy: IFolder = Object.assign({}, folder, {
            created: folder.created != null && folder.created.isValid() ? folder.created.toJSON() : null,
            modified: folder.modified != null && folder.modified.isValid() ? folder.modified.toJSON() : null
        });
        return copy;
    }

    protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
        if (res.body) {
            res.body.created = res.body.created != null ? moment(res.body.created) : null;
            res.body.modified = res.body.modified != null ? moment(res.body.modified) : null;
        }
        return res;
    }

    protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        if (res.body) {
            res.body.forEach((folder: IFolder) => {
                folder.created = folder.created != null ? moment(folder.created) : null;
                folder.modified = folder.modified != null ? moment(folder.modified) : null;
            });
        }
        return res;
    }
}
