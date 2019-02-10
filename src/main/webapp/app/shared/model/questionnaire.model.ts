export interface IQuestionnaire {
    id?: number;
    type?: string;
    description?: string;
    libele?: string;
}

export class Questionnaire implements IQuestionnaire {
    constructor(public id?: number, public type?: string, public description?: string, public libele?: string) {}
}
