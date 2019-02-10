import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IEtablissement } from 'app/shared/model/etablissement.model';

type EntityResponseType = HttpResponse<IEtablissement>;
type EntityArrayResponseType = HttpResponse<IEtablissement[]>;

@Injectable({ providedIn: 'root' })
export class EtablissementService {
    public resourceUrl = SERVER_API_URL + 'api/etablissements';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/etablissements';

    constructor(protected http: HttpClient) {}

    create(etablissement: IEtablissement): Observable<EntityResponseType> {
        return this.http.post<IEtablissement>(this.resourceUrl, etablissement, { observe: 'response' });
    }

    update(etablissement: IEtablissement): Observable<EntityResponseType> {
        return this.http.put<IEtablissement>(this.resourceUrl, etablissement, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IEtablissement>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IEtablissement[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IEtablissement[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}
