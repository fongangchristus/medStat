import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IMedecin } from 'app/shared/model/medecin.model';

type EntityResponseType = HttpResponse<IMedecin>;
type EntityArrayResponseType = HttpResponse<IMedecin[]>;

@Injectable({ providedIn: 'root' })
export class MedecinService {
    public resourceUrl = SERVER_API_URL + 'api/medecins';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/medecins';

    constructor(protected http: HttpClient) {}

    create(medecin: IMedecin): Observable<EntityResponseType> {
        return this.http.post<IMedecin>(this.resourceUrl, medecin, { observe: 'response' });
    }

    update(medecin: IMedecin): Observable<EntityResponseType> {
        return this.http.put<IMedecin>(this.resourceUrl, medecin, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IMedecin>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IMedecin[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IMedecin[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}
