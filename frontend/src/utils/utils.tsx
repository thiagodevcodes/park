const validateCPF = (cpf: string): boolean => {
    // Remove caracteres não numéricos
    cpf = cpf.replace(/[^\d]+/g, '');
    
    // Verifica se tem 11 dígitos
    if (cpf.length !== 11) return false;
    
    // Verifica se todos os dígitos são iguais
    if (/^(\d)\1{10}$/.test(cpf)) return false;
    
    // Valida os dois últimos dígitos verificadores
    const digits = cpf.split('').map(Number);
    const verifyDigit = (digits: number[], factor: number) => {
      const sum = digits.slice(0, factor).reduce((acc, digit, i) => acc + digit * (factor + 1 - i), 0);
      const remainder = (sum * 10) % 11;
      return remainder === 10 ? 0 : remainder;
    };
    
    const firstVerifier = verifyDigit(digits, 9);
    if (firstVerifier !== digits[9]) return false;
  
    const secondVerifier = verifyDigit(digits, 10);
    if (secondVerifier !== digits[10]) return false;
  
    return true;
  };


export {validateCPF}