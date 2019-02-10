export interface IReponse {
    id?: number;
    libele?: string;
    questionReponseId?: number;
}

export class Reponse implements IReponse {
    constructor(public id?: number, public libele?: string, public questionReponseId?: number) {}
}
