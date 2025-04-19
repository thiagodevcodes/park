import {
    Pagination,
    PaginationContent,
    PaginationEllipsis,
    PaginationItem,
    PaginationLink,
    PaginationNext,
    PaginationPrevious
} from "@/components/ui/pagination"

interface PaginationBoxProps {
    currentPage: number;
    setCurrentPage: (page: number) => void;
    totalPages: number;
    size: number;
}

const PaginationBox: React.FC<PaginationBoxProps> = ({ currentPage, setCurrentPage, totalPages, size }) => {
    const handlePageChange = (page: number) => {
        if (page >= 0 && page < totalPages) {
            setCurrentPage(page);
        }
    };

    const createPaginationItems = () => {
        const items = [];
        const maxPages = 3; // Número máximo de páginas a serem exibidas
        const totalPagesToShow = Math.min(totalPages, maxPages);

        // Adiciona o botão de página anterior
        items.push(
            <PaginationItem key="prev" className={currentPage === 0 ? 'disabled' : ''}>
                <PaginationPrevious
                    onClick={() => currentPage > 0 && handlePageChange(currentPage - 1)}
                />
            </PaginationItem>
        );

        // Calcular o intervalo de páginas a serem exibidas
        let startPage = Math.max(0, currentPage - Math.floor(maxPages / 2));
        let endPage = Math.min(totalPages - 1, startPage + maxPages - 1);

        // Ajustar o intervalo de início se for necessário
        if (endPage - startPage + 1 < totalPagesToShow) {
            startPage = Math.max(0, endPage - totalPagesToShow + 1);
        }

        // Adiciona os números das páginas
        for (let page = startPage; page <= endPage; page++) {
            items.push(
                <PaginationItem key={page}>
                    <PaginationLink
                        isActive={page === currentPage}
                        onClick={() => handlePageChange(page)}
                    >
                        {page + 1}
                    </PaginationLink>
                </PaginationItem>
            );
        }

        // Adiciona elipses se necessário
        if (endPage < totalPages - 1) {
            items.push(
                <PaginationItem key="ellipsis">
                    <PaginationEllipsis />
                </PaginationItem>
            );
        }

        // Adiciona o botão de página seguinte
        items.push(
            <PaginationItem key="next" className={currentPage === totalPages - 1 ? 'disabled' : ''}>
                <PaginationNext
                    onClick={() => currentPage < totalPages - 1 && handlePageChange(currentPage + 1)}
                />
            </PaginationItem>
        );

        return items;
    };

    return (
        <Pagination>
            <PaginationContent>
                {createPaginationItems()}
            </PaginationContent>
        </Pagination>
    );
};

export default PaginationBox;