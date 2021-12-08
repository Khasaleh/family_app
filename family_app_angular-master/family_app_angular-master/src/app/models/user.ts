import { BooleanLiteral } from "typescript";

export class User {
  id: number;
  username: string;
  password: string;
  email: string;
  roles:string[];
  enabled?:boolean;
}