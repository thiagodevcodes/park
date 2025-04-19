interface TableProps {
    columns: string[];
    children: React.ReactNode;
    width?: string | number;
}

const Table: React.FC<TableProps> = ({
    columns,
    children,
}) => {

    return (
        <table className="w-[1000px] my-5 min-w-[700px] rounded-lg bg-white">
            <thead className=" text-white">
                <tr className="">
                    {columns.map((item, index) => (
                        <th className={`p-3 bg-black ${index === 0 && "rounded-ss-lg"}`} key={index}>{item}</th>
                    ))}
                    <th className="p-3 rounded-se-lg bg-black">Ações</th>
                </tr>
            </thead>
            <tbody>
                {children}
            </tbody>
        </table> 
    );
};

export default Table;