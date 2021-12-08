import { User } from "./user";


export class ImageModel {

  id: number;
  date: Date;
  extension: string;
  user: User;
  localPath: string;
  checked?: boolean;
}
