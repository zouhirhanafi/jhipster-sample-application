export interface IParameter {
  id?: number;
  label?: string | null;
  activated?: boolean | null;
  lib2?: string | null;
  lib3?: string | null;
  refExterne?: string | null;
  val1?: string | null;
  val2?: string | null;
  val3?: string | null;
  ordre?: number | null;
  type?: IParameter | null;
  paraent?: IParameter | null;
}

export const defaultValue: Readonly<IParameter> = {
  activated: false,
};
