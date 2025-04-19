import React from 'react';
import styles from "./spinner.module.css"; // Importe o arquivo CSS do Spinner

const Spinner: React.FC = () => {
  return (
    <div className={styles.spinner}></div>
  );
}

export default Spinner;