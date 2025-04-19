import { Control, Controller } from "react-hook-form";
import { Input } from "../ui/input";

interface FormFieldProps {
  name: string;
  control: Control<any>;
  placeholder: string;
  type?: string;
  value?: any;
}


const FormField: React.FC<FormFieldProps> = ({ name, control, placeholder, type = "text", value }) => (
  <Controller
    name={name}
    control={control}
    defaultValue={type === 'number' ? '' : value} // Valor padrão para números é uma string vazia
    render={({ field }) => (
      <Input
        type={type}
        placeholder={placeholder}
        {...field}
        // Ajuste o valor e garanta que seja um número ou uma string vazia
        value={type === 'number' ? (field.value !== undefined && field.value !== null ? field.value : '') : field.value}
        onChange={(e) => {
          // Para números, converta o valor para número ou defina como uma string vazia se estiver vazio
          const inputValue = type === 'number' ? (e.target.value === '' ?  '' : Number(e.target.value)) : e.target.value;
          field.onChange(inputValue);
        }}
      />
    )}
  />
);


export default FormField;