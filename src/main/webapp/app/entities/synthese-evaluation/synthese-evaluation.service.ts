import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ISyntheseEvaluation } from 'app/shared/model/synthese-evaluation.model';

type EntityResponseType = HttpResponse<ISyntheseEvaluation>;
type EntityArrayResponseType = HttpResponse<ISyntheseEvaluation[]>;

@Injectable({ providedIn: 'root' })
export class SyntheseEvaluationService {
    public resourceUrl = SERVER_API_URL + 'api/synthese-evaluations';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/synthese-evaluations';

    constructor(protected http: HttpClient) {}

    create(syntheseEvaluation: ISyntheseEvaluation): Observable<EntityResponseType> {
        return this.http.post<ISyntheseEvaluation>(this.resourceUrl, syntheseEvaluation, { observe: 'response' });
    }

    update(syntheseEvaluation: ISyntheseEvaluation): Observable<EntityResponseType> {
        return this.http.put<ISyntheseEvaluation>(this.resourceUrl, syntheseEvaluation, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<ISyntheseEvaluation>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ISyntheseEvaluation[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ISyntheseEvaluation[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}
